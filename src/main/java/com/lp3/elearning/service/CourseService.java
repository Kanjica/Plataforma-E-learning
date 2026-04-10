package com.lp3.elearning.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.lp3.elearning.repository.CourseRepository;

@Service
public class CourseService {

    private final ModuleService moduleService;
    private final CourseRepository courseRepository;
    private final CategoriesService categoriesService;
    private final InstructorService instructorService;

    public CourseService(CourseRepository courseRepository, 
        CategoriesService categoriesService, 
        @Lazy InstructorService instructorService, 
        @Lazy ModuleService moduleService) {
        this.courseRepository = courseRepository;
        this.categoriesService = categoriesService;
        this.instructorService = instructorService;
        this.moduleService = moduleService;
    }

    @Transactional
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

        Course course = toEntity(request, categories, instructors);
        
        return toResponseDTO(courseRepository.save(course));
    }

    /**
     * Filtra cursos por título e/ou categorias.
     * Implementação otimizada para evitar N+1 selects.
     */
    @Transactional(readOnly = true)
    public Set<CourseResponseDTO> filterCourses(CourseFilterDTO request){
        String title = request.title() != null ? request.title() : "";
        Set<Long> categoryIds = request.categoryIds() != null ? request.categoryIds() : Collections.emptySet();
        
        List<Course> courses;

        if(categoryIds.isEmpty()){
            courses = courseRepository.findByTitleContainingIgnoreCase(title);
        } else if (title.isEmpty()){
            courses = courseRepository.findByCategories_IdIn(categoryIds);
        } else {
            courses = courseRepository.findByTitleContainingIgnoreCaseAndCategories_IdIn(title, categoryIds);
        }
        
        return courses.stream().map(this::toResponseDTO).collect(Collectors.toSet());
    }

    @Transactional
    public CourseResponseDTO updateCourse(Long id, CourseRequestDTO request) {
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

        updateCourseData(existingCourse, request, categories, instructors);
        return toResponseDTO(courseRepository.save(existingCourse));
    }

    private void updateCourseData(Course course, CourseRequestDTO request, Set<Category> cats, Set<Instructor> insts) {
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setWorkload(request.workload());
        course.setImageUrl(request.imageUrl());
        course.setPrice(request.price());
        course.setOldPrice(request.oldPrice());
        course.setIsBestSeller(request.isBestSeller());
        course.setCategories(cats);
        course.setInstructors(insts);
    }
    
    // --- Métodos Auxiliares ---

    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com ID: " + id));
    }

    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Curso não encontrado.");
        }
        courseRepository.deleteById(id);
    }

    public CourseResponseDTO getCourseByIdResponseDTO(Long id) {
        return toResponseDTO(findById(id));
    }

    public Page<CourseResponseDTO> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
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

    // Converters
    public CourseResponseDTO toResponseDTO(Course course) {
        return new CourseResponseDTO(
            course.getId(), course.getTitle(), course.getDescription(), course.getWorkload(),
            course.getCategories().stream().map(categoriesService::toResponseDTO).collect(Collectors.toSet()),
            course.getInstructors().stream().map(instructorService::toResponseDTO).collect(Collectors.toSet()),
            course.getImageUrl(),
            course.getModules() != null ? course.getModules().stream().map(moduleService::toResponseDTO).collect(Collectors.toSet()) : Collections.emptySet(),
            course.getPrice(), course.getOldPrice(), course.getIsBestSeller()
        );
    }

    private Course toEntity(CourseRequestDTO request, Set<Category> categories, Set<Instructor> instructors) {
        return Course.builder()
            .title(request.title()).description(request.description())
            .workload(request.workload()).imageUrl(request.imageUrl())
            .price(request.price()).oldPrice(request.oldPrice())
            .isBestSeller(request.isBestSeller())
            .categories(categories).instructors(instructors).build();
    }
}