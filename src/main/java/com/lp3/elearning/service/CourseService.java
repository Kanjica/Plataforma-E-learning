package com.lp3.elearning.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.course.CourseFilterDTO;
import com.lp3.elearning.dto.course.CourseListDTO;
import com.lp3.elearning.dto.course.CourseRequestDTO;
import com.lp3.elearning.dto.course.CourseResponseDTO;
import com.lp3.elearning.entities.Category;
import com.lp3.elearning.entities.Course;
import com.lp3.elearning.entities.Instructor;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.exception.ConflictException;
import com.lp3.elearning.exception.ResourceNotFoundException;
import com.lp3.elearning.mapper.CourseMapper;
import com.lp3.elearning.repository.CourseRepository;
import com.lp3.elearning.security.anottation.Auditable;
import com.lp3.elearning.specification.CourseSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "courses")
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryService categoriesService;
    private final InstructorService instructorService;
    private final CourseMapper courseMapper;

    @Transactional
    @Auditable(action = "CRIAR_CURSO")
    @CacheEvict(allEntries = true)
    public CourseResponseDTO createCourse(CourseRequestDTO request, User user) {
        if(courseRepository.existsByTitle(request.title())){
            throw new ConflictException("Já existe um curso com o título: " + request.title());
        }

        Set<Category> categories = categoriesService.getCategoriesByValidIds(request.categoryIds());
        if(categories.isEmpty()) throw new BusinessRuleException("O curso deve ter pelo menos uma categoria.");

        Set<Instructor> instructors = instructorService.getInstructorsByValidIds(request.instructorIds());
        
        if(instructors.isEmpty() && !(user instanceof Instructor)){
            throw new BusinessRuleException("É necessário informar os instrutores do curso.");
        }

        if(instructors.isEmpty()){
            instructors.add((Instructor) user);
        }

        Course course = courseMapper.toEntity(request);
        course.setCategories(categories);
        course.setInstructors(instructors);
        
        return courseMapper.toResponseDTO(courseRepository.save(course));
    }

    /**
     * Filtra cursos por título e/ou categorias.
     * Implementação otimizada para evitar N+1 selects.
     */
    @Transactional(readOnly = true)
    @Cacheable(key = "'filter-' + #request.title + '-' + T(java.util.Objects).hashCode(#request.categoryIds) + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<CourseListDTO> filterCourses(CourseFilterDTO request, Pageable pageable) {

        Specification<Course> spec = Specification.allOf(
                    CourseSpecification.hasTitle(request.title()),
                    CourseSpecification.hasCategoryIds(request.categoryIds())
                );
        
        return courseRepository.findAll(spec,pageable)
            .map(courseMapper::toListDTO);
    }

    public Set<CourseResponseDTO> findCoursesByInstructorId(Long instructorId) {
        Instructor instructor = instructorService.findInstructorEntityById(instructorId);
        return instructor.getCourses().stream()
            .map(course -> courseMapper.toResponseDTO(course))
            .collect(Collectors.toSet());
    }
    
    @Transactional
    @Auditable(action = "ATUALIZAR_CURSO")
    public CourseResponseDTO update(Long id, CourseRequestDTO request) {
        Course existingCourse = findById(id);
        
        // Verifica duplicidade de título apenas se o título mudou
        if(!existingCourse.getTitle().equals(request.title()) && courseRepository.existsByTitle(request.title())){
            throw new ConflictException("Já existe outro curso com este título.");
        }

        Set<Category> categories = categoriesService.getCategoriesByValidIds(request.categoryIds());
        Set<Instructor> instructors = instructorService.getInstructorsByValidIds(request.instructorIds());

        if(categories.isEmpty() || instructors.isEmpty()){
            throw new BusinessRuleException("Categorias e Instrutores não podem ser vazios.");
        }

        Course course = courseMapper.updateCourseFromRequestDTO(request, existingCourse);

        course.setCategories(categories);
        course.setInstructors(instructors);

        CourseResponseDTO response = courseMapper.toResponseDTO(courseRepository.save(course));
        this.executeEvictCourse(id);
        return response;
    }

    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com ID: " + id));
    }

    @Auditable(action = "DELETAR_CURSO")
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Curso não encontrado.");
        }
        courseRepository.deleteById(id);
        this.executeEvictCourse(id);
    }

    @Cacheable(key = "#id")
    public CourseResponseDTO getById(Long id) {
        return courseMapper.toResponseDTO(findById(id));
    }

    @Cacheable(key = "'paged-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<CourseResponseDTO> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).map(courseMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "'list-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<CourseListDTO> findAllPaged(Pageable pageable) {
        // 1ª Query: Busca a página de cursos (ex: 20 registros)
        Page<Course> coursePage = courseRepository.findAll(pageable);

        // O .map() do Page mantém os metadados (total de páginas, etc)
        return coursePage.map(course -> new CourseListDTO(
            course.getId(),
            course.getTitle(),
            // Aqui o BatchSize entra em ação: 
            // Na primeira iteração, o Hibernate busca as categorias/instrutores 
            // de TODOS os cursos da página de uma vez.
            course.getCategories().stream().map(Category::getName).toList(),
            course.getInstructors().stream().map(Instructor::getName).toList()
        ));
    }

    @Caching(evict = {
        @CacheEvict(key = "#id"),
        @CacheEvict(allEntries = true)
    })
    public void executeEvictCourse(Long id) {}
}