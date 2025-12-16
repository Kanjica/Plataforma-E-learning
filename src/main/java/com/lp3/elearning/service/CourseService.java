package com.lp3.elearning.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.CourseFilterDTO;
import com.lp3.elearning.dto.CourseRequestDTO;
import com.lp3.elearning.dto.CourseResponseDTO;
import com.lp3.elearning.entities.Category;
import com.lp3.elearning.entities.Course;
import com.lp3.elearning.entities.Instructor;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.exception.ConflictException;
import com.lp3.elearning.repository.CategoriesRepository;
import com.lp3.elearning.repository.CourseRepository;

@Service
public class CourseService {

    private final CategoriesRepository categoriesRepository;

    private final ModuleService moduleService;

    private final CourseRepository courseRepository;
    private final CategoriesService categoriesService;
    private final InstructorService instructorService;

    public CourseService(CourseRepository courseRepository, 
        CategoriesService categoriesService, 
        @Lazy InstructorService instructorService, 
        @Lazy ModuleService moduleService, 
        @Lazy LessonService lessonService, CategoriesRepository categoriesRepository) {
        this.courseRepository = courseRepository;
        this.categoriesService = categoriesService;
        this.instructorService = instructorService;
        this.moduleService = moduleService;
        this.categoriesRepository = categoriesRepository;
    }

    @Transactional
    public CourseResponseDTO createCourse(CourseRequestDTO request) {
        if(alreadyExists(request)){
            throw new ConflictException("Já existe um curso com o título: " + request.title());
        }

        // Busca as categorias e instrutores usando os Services auxiliares
        Set<Category> categories = categoriesService.getCategoriesByValidIds(request.categoryIds());
        Set<Instructor> instructors = instructorService.getInstructorsByValidIds(request.instructorIds());

        // Validação básica
        if(categories.isEmpty()){
            throw new BusinessRuleException("O curso deve ser associado a pelo menos uma categoria válida.");
        }
        
        // Se a lista de instrutores vier vazia no JSON, tentamos pegar o usuário logado
        if(instructors.isEmpty()){
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user instanceof Instructor instructor) {
                instructors.add(instructor);
            } else {
                throw new BusinessRuleException("O curso deve ter pelo menos um instrutor válido.");
            }
        }

        Course course = Course.builder()
                .title(request.title())
                .description(request.description())
                .workload(request.workload())
                .categories(categories)
                .instructors(instructors)
                .build();

        return toResponseDTO(courseRepository.save(course));
    }

    public Set<CourseResponseDTO> filterCourses(CourseFilterDTO request){
        
        String title = request.title() != null ? request.title() : "";
        Set<Long> categoryIds = request.categoryIds() != null ? request.categoryIds() : Collections.emptySet();
        
        List<Course> courses;

        if(categoryIds.isEmpty()){
            courses = courseRepository.findByTitleContainingIgnoreCase(title);
        }else if (title.isEmpty()){
            courses = courseRepository.findByCategories_IdIn(categoryIds);
        }else{
            courses = courseRepository.findByTitleContainingIgnoreCaseAndCategories_IdIn(title, categoryIds);
        }
        
        return courses.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toSet());
    }

    @Transactional
    public CourseResponseDTO updateCourse(Long id, CourseRequestDTO request) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Curso com ID " + id + " não encontrado."));

        if(!existingCourse.getTitle().equals(request.title()) && alreadyExists(request)){
            throw new ConflictException("Já existe um curso com o título: " + request.title());
        }

        Set<Category> categories = categoriesService.getCategoriesByValidIds(request.categoryIds());
        Set<Instructor> instructors = instructorService.getInstructorsByValidIds(request.instructorIds());

        if(!verifyCategoriesAndInstructors(categories, instructors)){
            throw new BusinessRuleException("O curso deve ser associado a pelo menos uma categoria válida e ter pelo menos um instrutor válido.");
        }

        existingCourse.setTitle(request.title());
        existingCourse.setDescription(request.description());
        existingCourse.setWorkload(request.workload());
        existingCourse.setCategories(categories);
        existingCourse.setInstructors(instructors);

        return toResponseDTO(courseRepository.save(existingCourse));
    }

    public boolean verifyCategoriesAndInstructors(Set<Category> categories, Set<Instructor> instructors) {
        return !categories.isEmpty() && !instructors.isEmpty();
    }
    
    public CourseResponseDTO toResponseDTO(Course course) {
        return new CourseResponseDTO(
            course.getId(),
            course.getTitle(),
            course.getDescription(),
            course.getWorkload(),
            course.getCategories().stream().map(categoriesService::toResponseDTO).collect(Collectors.toSet()),
            course.getInstructors().stream().map(instructorService::toResponseDTO).collect(Collectors.toSet()),
            course.getImageUrl(),
            course.getModules().stream().map(moduleService::toResponseDTO).collect(Collectors.toSet())
        );
    }

    public Course toEntity(CourseRequestDTO request) {
        Set<Category> categories = categoriesService.getCategoriesByValidIds(request.categoryIds());
        Set<Instructor> instructors = instructorService.getInstructorsByValidIds(request.instructorIds());

        return Course.builder()
                .title(request.title())
                .description(request.description())
                .workload(request.workload())
                .categories(categories)
                .instructors(instructors)
                .build();
    }
    
    public Course toEntity(CourseRequestDTO request, Set<Category> categories, Set<Instructor> instructors) {
        return Course.builder()
                .title(request.title())
                .description(request.description())
                .workload(request.workload())
                .categories(categories)
                .instructors(instructors)
                .build();
    }

    public boolean alreadyExists(CourseRequestDTO request) {
        return courseRepository.existsByTitle(request.title());
    }

    public CourseResponseDTO getCourseByIdResponseDTO(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Curso com ID " + id + " não encontrado."));
        return toResponseDTO(course);
    }

    public java.util.List<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteCourse(Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
        } else {
            throw new BusinessRuleException("Curso não encontrado para deleção.");
        }
    }

    // Adicione este método para permitir que outros Services busquem a Entidade Curso
    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Curso não encontrado com o ID: " + id));
    }

}