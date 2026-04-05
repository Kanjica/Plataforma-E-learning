package com.lp3.elearning.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lp3.elearning.dto.course.LessonResponseDTO;
import com.lp3.elearning.entities.*;
import com.lp3.elearning.entities.Module;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.repository.LessonRepository;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @InjectMocks
    private LessonService lessonService;

    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private EnrollmentService enrollmentService;
    @Mock
    private CompletedLessonsService completedLessonsService;
    @Mock
    private ModuleService moduleService;

    @Test
    @DisplayName("Deve permitir acesso se for a primeira aula do primeiro módulo")
    void shouldAllowAccess_FirstLesson() {
        // Cenário
        Long studentId = 1L;
        Long courseId = 10L;
        Long lessonId = 100L;

        Course course = new Course();
        course.setId(courseId);

        Module module = new Module();
        module.setId(50L);
        module.setModuleOrder(1);
        module.setCourse(course);

        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setLessonOrder(1);
        lesson.setModule(module);

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);

        // Mocks
        when(enrollmentService.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(enrollment);
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        // Execução
        LessonResponseDTO result = lessonService.getLessonByIdForUser(lessonId, studentId);

        // Verificação
        assertNotNull(result);
        assertEquals(lessonId, result.id());
    }

    @Test
    @DisplayName("Deve BLOQUEAR se tentar acessar a Aula 2 sem fazer a Aula 1")
    void shouldBlock_SkipLessonInSameModule() {
        // Cenário
        Long studentId = 1L;
        Long courseId = 10L;
        
        Course course = new Course(); course.setId(courseId);
        Module module = new Module(); module.setId(50L); module.setCourse(course);

        Lesson aula2 = new Lesson(); 
        aula2.setId(102L); 
        aula2.setLessonOrder(2); 
        aula2.setModule(module);

        Lesson aula1 = new Lesson(); 
        aula1.setId(101L); 
        aula1.setTitle("Aula 1");

        Enrollment enrollment = new Enrollment(); enrollment.setCourse(course);

        // Mocks
        when(enrollmentService.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(enrollment);
        when(lessonRepository.findById(102L)).thenReturn(Optional.of(aula2));
        
        // Simula busca da aula anterior
        when(lessonRepository.findByModuleIdAndLessonOrder(module.getId(), 1)).thenReturn(Optional.of(aula1));
        
        // O PULO DO GATO: Simula que a aula 1 NÃO está completa
        when(completedLessonsService.isLessonCompleted(enrollment, aula1)).thenReturn(false);

        // Execução e Verificação
        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            lessonService.getLessonByIdForUser(102L, studentId);
        });

        assertTrue(exception.getMessage().contains("concluir a aula 'Aula 1'"), "Mensagem de erro deve ser clara");
    }

    @Test
    @DisplayName("Deve BLOQUEAR se o curso da matrícula não bater com o da aula")
    void shouldBlock_WrongCourse() {
        Course cursoA = new Course(); cursoA.setId(1L);
        Course cursoB = new Course(); cursoB.setId(2L);

        Module module = new Module(); module.setCourse(cursoA);
        Lesson lesson = new Lesson(); lesson.setModule(module);

        Enrollment enrollment = new Enrollment(); 
        enrollment.setCourse(cursoB); // Aluno matriculado no curso B tenta ver aula do A

        when(enrollmentService.findByStudentIdAndCourseId(any(), any())).thenReturn(enrollment);
        when(lessonRepository.findById(any())).thenReturn(Optional.of(lesson));

        assertThrows(BusinessRuleException.class, () -> {
            lessonService.getLessonByIdForUser(1L, 1L);
        });
    }
}