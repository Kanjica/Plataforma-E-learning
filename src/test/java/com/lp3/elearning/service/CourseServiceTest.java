package com.lp3.elearning.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lp3.elearning.dto.course.CourseRequestDTO;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.exception.ConflictException;
import com.lp3.elearning.repository.CourseRepository;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CategoriesService categoriesService;
    @Mock
    private InstructorService instructorService;

    @Test
    void shouldThrowException_WhenCreatingCourseWithoutCategory() {
        CourseRequestDTO request = new CourseRequestDTO(
            "Java Basics", "Desc", 10, 
            Set.of(1L), // ID Categoria
            Set.of(1L), // ID Instrutor
            "img", null, null, false
        );

        when(courseRepository.existsByTitle(any())).thenReturn(false);
        // Simula que o serviço de categorias retornou vazio (ID inválido ou nenhum encontrado)
        when(categoriesService.getCategoriesByValidIds(any())).thenReturn(Collections.emptySet());

        assertThrows(BusinessRuleException.class, () -> {
            courseService.createCourse(request);
        });
    }

    @Test
    void shouldThrowConflict_WhenTitleExists() {
        CourseRequestDTO request = new CourseRequestDTO(
            "Java Basics", "Desc", 10, 
            new HashSet<>(), new HashSet<>(), 
            "img", null, null, false
        );

        when(courseRepository.existsByTitle("Java Basics")).thenReturn(true);

        assertThrows(ConflictException.class, () -> {
            courseService.createCourse(request);
        });
    }
}