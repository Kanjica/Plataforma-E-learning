package com.lp3.elearning.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lp3.elearning.entities.CompletedLesson;
import com.lp3.elearning.entities.Course;
import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.entities.Lesson;
import com.lp3.elearning.entities.Module;
import com.lp3.elearning.repository.CompletedLessonRepository;
import com.lp3.elearning.repository.EnrollmentRepository;

@ExtendWith(MockitoExtension.class)
class CompletedLessonsServiceTest {

    @InjectMocks private CompletedLessonsService completedService;
    
    @Mock private CompletedLessonRepository repository;
    @Mock private EnrollmentService enrollmentService;
    @Mock private LessonService lessonService;
    @Mock private EnrollmentRepository enrollmentRepository;
    @Mock private ProgressService progressService;
    @Mock private LearningProgressService learningProgressService;

    @Test
    void shouldCompleteLesson_AndSaveProgress() {
        // Setup do Cenário
        Course course = new Course(); 
        course.setId(10L);

        Module module = new Module(); 
        module.setId(5L); 
        module.setCourse(course);

        Enrollment enrollment = new Enrollment(); 
        enrollment.setId(1L); 
        enrollment.setCourse(course);

        Lesson lesson = new Lesson(); 
        lesson.setId(100L); 
        lesson.setModule(module);
            
        lesson.setLessonOrder(1); // Vamos testar a primeira aula para simplificar

        when(enrollmentService.findByStudentIdAndCourseId(anyLong(), anyLong())).thenReturn(enrollment);
        when(lessonService.findById(100L)).thenReturn(lesson);
        
        // Simula o cálculo do progresso
        when(progressService.calculateOverallProgress(anyLong(), anyLong())).thenReturn(0.5);

        // EXECUÇÃO: Você deve chamar o método do serviço que está testando
        // Se quiser testar o fluxo completo, o @InjectMocks deveria ser no LearningProgressService
        learningProgressService.completeLesson(1L, 10L, 100L);

        // VERIFICAÇÃO: Ajustada para bater com a nova assinatura
        // Como é a aula 1, a anterior é null
        verify(progressService).validateLessonAccessibility(eq(lesson), eq(enrollment), isNull());
        
        verify(repository, times(1)).save(any(CompletedLesson.class));
    }
}