package com.lp3.elearning.service;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.enrollment.CompletedLessonResponseDTO;
import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.entities.Lesson;
import com.lp3.elearning.entities.StatusEnrollment;
import com.lp3.elearning.exception.BusinessRuleException;
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

    @Transactional
    public CompletedLessonResponseDTO completeLesson(Long studentId, Long courseId, Long lessonId) {
        // 1. Busca dados básicos
        Enrollment enrollment = enrollmentService.findByStudentIdAndCourseId(studentId, courseId);
        Lesson lesson = lessonService.findById(lessonId);

        // 2. Valida se pode concluir (Regra de Negócio)
        validateLessonAccessibility(lesson, enrollment);

        // 3. Registra a conclusão
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

    public void validateLessonAccessibility(Lesson currentLesson, Enrollment enrollment) {
        if (!enrollment.getCourse().getId().equals(currentLesson.getModule().getCourse().getId())) {
            throw new BusinessRuleException("Acesso negado: Esta aula não pertence ao curso.");
        }

        Integer currentOrder = currentLesson.getLessonOrder();
        
        // Regra: Se não for a primeira aula, a anterior deve estar concluída
        if (currentOrder > 1) {
            Lesson previous = lessonService.findByModuleAndOrder(currentLesson.getModule().getId(), currentOrder - 1);
            if (!completedLessonsService.isLessonCompleted(enrollment, previous)) {
                throw new BusinessRuleException("Bloqueado: Conclua a aula anterior.");
            }
        }
    }
}