package com.lp3.elearning.service;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.enrollment.CompletedLessonResponseDTO;
import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.entities.Lesson;
import com.lp3.elearning.entities.StatusEnrollment;
import com.lp3.elearning.mapper.CompletedLessonMapper;
import com.lp3.elearning.repository.EnrollmentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearningProgressService {
    private final LessonService lessonService;
    private final EnrollmentService enrollmentService;
    private final CompletedLessonsService completedLessonsService;
    private final EnrollmentRepository enrollmentRepository;
    private final CompletedLessonMapper completedLessonMapper;
    private final ProgressService progressService; // Adicionado

    @Transactional
    public CompletedLessonResponseDTO completeLesson(Long studentId, Long courseId, Long lessonId) {
        Enrollment enrollment = enrollmentService.findByStudentIdAndCourseId(studentId, courseId);
        Lesson lesson = lessonService.findById(lessonId);

        // Busca aula anterior ANTES de validar
        Lesson previous = null;
        if (lesson.getLessonOrder() > 1) {
            previous = lessonService.findByModuleAndOrder(lesson.getModule().getId(), lesson.getLessonOrder() - 1);
        }

        progressService.validateLessonAccessibility(lesson, enrollment, previous);

        var completedLesson = completedLessonsService.saveCompletion(enrollment, lesson);
        // 4. Atualiza o progresso da matrícula (Orquestração)
        double newProgress = enrollmentService.calculateOverallProgress(enrollment);
        enrollment.setOverallProgress(newProgress);
        
        if (newProgress >= 1.0) {
            enrollment.setStatus(StatusEnrollment.COMPLETED);
        }
        enrollmentRepository.save(enrollment);

        return completedLessonMapper.toResponseDTO(completedLesson);
    }

}