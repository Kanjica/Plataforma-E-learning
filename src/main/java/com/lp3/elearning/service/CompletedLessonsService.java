package com.lp3.elearning.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.lp3.elearning.repository.EnrollmentRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.enrollment.CompletedLessonResponseDTO;
import com.lp3.elearning.entities.CompletedLesson;
import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.entities.Lesson;
import com.lp3.elearning.entities.StatusEnrollment;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.mapper.CompletedLessonMapper;
import com.lp3.elearning.repository.CompletedLessonRepository;

@Service 
public class CompletedLessonsService {

    private final EnrollmentRepository enrollmentRepository;

    private final CompletedLessonRepository completedLessonRepository;
    private final EnrollmentService enrollmentService;
    private final LessonService lessonService;
    private final CompletedLessonMapper completedLessonMapper;

    public CompletedLessonsService(
            CompletedLessonRepository completedLessonRepository, 
            LessonService lessonService, 
            @Lazy EnrollmentService enrollmentService, 
            EnrollmentRepository enrollmentRepository, 
            CompletedLessonMapper completedLessonMapper) {
        this.completedLessonRepository = completedLessonRepository;
        this.lessonService = lessonService;
        this.enrollmentService = enrollmentService;
        this.enrollmentRepository = enrollmentRepository;
        this.completedLessonMapper = completedLessonMapper;
    }

    @Transactional
    public CompletedLessonResponseDTO completeLesson(Long studentId, Long courseId, Long lessonId){

        Enrollment existingEnrollment = enrollmentService.findByStudentIdAndCourseId(studentId, courseId);
        Lesson lesson = lessonService.findById(lessonId);

        lessonService.validateLessonAccessibility(lesson, existingEnrollment);

        if(isLessonCompleted(existingEnrollment, lesson)){
             throw new BusinessRuleException("Esta aula já foi concluída anteriormente.");
        }

        CompletedLesson completedLesson = CompletedLesson.builder()
            .enrollment(existingEnrollment) 
            .lesson(lesson)
            .completionDate(LocalDateTime.now())
            .build();
        
        CompletedLesson savedLesson = completedLessonRepository.save(completedLesson);

        // Atualiza Progresso na Matrícula
        double newProgress = enrollmentService.calculateOverallProgress(existingEnrollment);
        existingEnrollment.setOverallProgress(newProgress);
        
        if (newProgress >= 1.0) {
            existingEnrollment.setStatus(StatusEnrollment.COMPLETED);
        }
        enrollmentRepository.save(existingEnrollment);

        return completedLessonMapper.toResponseDTO(savedLesson);
    }

    public boolean isLessonCompleted(Enrollment enrollment, Lesson lesson) {
        return completedLessonRepository.existsByEnrollmentAndLesson(enrollment, lesson);
    }

    public Integer countByEnrollment(Enrollment enrollment) {
        return completedLessonRepository.countByEnrollment(enrollment);
    }

    public Set<CompletedLessonResponseDTO> findByEnrollment(Enrollment enrollment) {
        return toResponseDTOs(completedLessonRepository.findByEnrollment(enrollment));
    }

    private Set<CompletedLessonResponseDTO> toResponseDTOs(List<CompletedLesson> completedLessons){
        return completedLessons.stream().map(completedLessonMapper::toResponseDTO).collect(Collectors.toSet());
    }
}