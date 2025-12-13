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
}

