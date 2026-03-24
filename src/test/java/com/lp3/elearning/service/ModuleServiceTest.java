package com.lp3.elearning.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lp3.elearning.dto.course.ModuleReorderRequestDTO;
import com.lp3.elearning.dto.course.ModuleRequestDTO;
import com.lp3.elearning.entities.Course;
import com.lp3.elearning.entities.Module;
import com.lp3.elearning.exception.ConflictException;
import com.lp3.elearning.repository.CourseRepository;
import com.lp3.elearning.repository.ModuleRepository;

@ExtendWith(MockitoExtension.class)
class ModuleServiceTest {

    @InjectMocks private ModuleService moduleService;
    @Mock private ModuleRepository moduleRepository;
    @Mock private CourseService courseService;
    @Mock private CourseRepository courseRepository;
    @Mock private LessonService lessonService; // Mock necessário para toResponseDTO

    @Test
    @DisplayName("Deve impedir criação de módulo com mesma ordem")
    void shouldThrowConflict_WhenOrderExists() {
        Long courseId = 1L;
        ModuleRequestDTO request = new ModuleRequestDTO("Módulo 2", "Desc", 1);
        Course course = new Course(); course.setId(courseId);

        when(courseService.findById(courseId)).thenReturn(course);
        when(moduleRepository.findByTitleAndCourseId(any(), any())).thenReturn(Optional.empty());
        when(moduleRepository.findByModuleOrderAndCourseId(1, courseId)).thenReturn(Optional.of(new Module()));

        assertThrows(ConflictException.class, () -> moduleService.create(request, courseId));
    }

    @Test
    @DisplayName("Deve reordenar módulos com sucesso")
    void shouldReorderModules() {
        Long courseId = 1L;
        Course course = new Course(); 
        course.setId(courseId);
        course.setTitle("Curso Teste");

        // CORREÇÃO: Adicionamos o curso aos módulos para evitar NPE no toResponseDTO
        Module m1 = Module.builder().id(10L).moduleOrder(1).course(course).build();
        Module m2 = Module.builder().id(11L).moduleOrder(2).course(course).build();
        
        List<ModuleReorderRequestDTO> requests = List.of(
            new ModuleReorderRequestDTO(10L, 2),
            new ModuleReorderRequestDTO(11L, 1)
        );

        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(moduleRepository.findByCourseId(courseId)).thenReturn(List.of(m1, m2));
        when(moduleRepository.saveAll(any())).thenAnswer(i -> i.getArgument(0));

        var result = moduleService.reorder(courseId, requests);

        assertEquals(2, result.size());
        verify(moduleRepository).saveAll(argThat(list -> {
            List<Module> modules = (List<Module>) list;
            return modules.stream().anyMatch(m -> m.getId() == 10L && m.getModuleOrder() == 2);
        }));
    }
}