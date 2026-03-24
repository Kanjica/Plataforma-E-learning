package com.lp3.elearning.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lp3.elearning.dto.enrollment.EnrollmentRequestDTO;
import com.lp3.elearning.entities.*;
import com.lp3.elearning.exception.ConflictException;
import com.lp3.elearning.repository.EnrollmentRepository;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private StudentService studentService;
    @Mock
    private CourseService courseService;
    @Mock
    private LessonService lessonService;
    @Mock
    private CompletedLessonsService completedLessonsService;

    @Test
    void shouldCalculateProgressCorrectly() {
        // Cenário: Curso com 4 aulas, Aluno fez 1
        Course course = new Course(); course.setId(10L);
        Enrollment enrollment = new Enrollment(); enrollment.setCourse(course);

        when(lessonService.countLessonsInCourse(10L)).thenReturn(4);
        when(completedLessonsService.countByEnrollment(enrollment)).thenReturn(1);

        // Execução
        Double progress = enrollmentService.calculateOverallProgress(enrollment);

        // Verificação: 1/4 = 0.25
        assertEquals(0.25, progress);
    }

    @Test
    void shouldPreventDuplicateEnrollment() {
        EnrollmentRequestDTO request = new EnrollmentRequestDTO(1L, 10L);

        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 10L)).thenReturn(true);

        assertThrows(ConflictException.class, () -> {
            enrollmentService.create(request);
        });
        
        // Garante que não tentou salvar nada
        verify(enrollmentRepository, never()).save(any());
    }
}