package com.lp3.elearning.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.entities.Lesson;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.repository.CompletedLessonRepository;
import com.lp3.elearning.repository.EnrollmentRepository;

// ProgressService.java
@Service
@RequiredArgsConstructor
public class ProgressService {
    private final EnrollmentRepository enrollmentRepository;
    private final CompletedLessonRepository completedLessonRepository;

    public Double calculateOverallProgress(Long enrollmentId, Long courseId) {
        Double progress = enrollmentRepository.getProgress(enrollmentId, courseId);
        return progress != null ? progress : 0.0;
    }

    // Movemos a lógica de validação para cá
    public void validateLessonAccessibility(Lesson currentLesson, Enrollment enrollment, Lesson previousLesson) {
        if (!enrollment.getCourse().getId().equals(currentLesson.getModule().getCourse().getId())) {
            throw new BusinessRuleException("Acesso negado: Esta aula não pertence ao curso.");
        }

        if (currentLesson.getLessonOrder() > 1 && previousLesson != null) {
            boolean completed = completedLessonRepository.existsByEnrollmentAndLesson(enrollment, previousLesson);
            if (!completed) {
                throw new BusinessRuleException("Bloqueado: Conclua a aula anterior.");
            }
        }
    }
}