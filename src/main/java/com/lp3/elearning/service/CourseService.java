package com.lp3.elearning.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.CourseRequestDTO;
import com.lp3.elearning.dto.CourseResponseDTO;
import com.lp3.elearning.entities.Category;
import com.lp3.elearning.entities.Course;
import com.lp3.elearning.entities.Instructor;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.exception.ConflictException;
import com.lp3.elearning.repository.CourseRepository;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoriesService categoriesService;
    private final InstructorService instructorService;

    public CourseService(CourseRepository courseRepository, CategoriesService categoriesService, InstructorService instructorService) {
        this.courseRepository = courseRepository;
        this.categoriesService = categoriesService;
        this.instructorService = instructorService;
    }

    public CourseResponseDTO createCourse(CourseRequestDTO request) {
        if(alreadyExists(request)){
            throw new ConflictException("Já existe um curso com o título: " + request.title());
        }

        Set<Category> categories = categoriesService.getCategoriesByValidIds(request.categoryIds());
        Set<Instructor> instructors = instructorService.getInstructorsByValidIds(request.instructorIds());

        if(categories.isEmpty()){
            throw new BusinessRuleException("O curso deve ser associado a pelo menos uma categoria válida.");
        }
        
        if(instructors.isEmpty()){
            throw new BusinessRuleException("O curso deve ter pelo menos um instrutor válido.");
        }

        Course course = toEntity(request, categories, instructors);
        return toResponseDTO(courseRepository.save(course));
    }

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
            course.getInstructors().stream().map(instructorService::toResponseDTO).collect(Collectors.toSet())
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

    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessRuleException("Curso com ID " + courseId + " não encontrado."));
    }

    public CourseResponseDTO getCourseByIdResponseDTO(Long id) {
        Course course = getCourseById(id);
        return toResponseDTO(course);
    }

    public java.util.List<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        if (course != null) {
            courseRepository.delete(course);
        }
    }
}

