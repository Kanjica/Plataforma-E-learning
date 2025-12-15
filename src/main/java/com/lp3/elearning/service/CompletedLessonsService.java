package com.lp3.elearning.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.CompletedLessonResponseDTO;
import com.lp3.elearning.dto.EnrollmentRequestDTO;
import com.lp3.elearning.entities.CompletedLesson;
import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.repository.CompletedLessonRepository;

@Service
public class CompletedLessonsService {

    private final CompletedLessonRepository completedLessonRepository;
    private final EnrollmentService enrollmentService;
    private final LessonService lessonService;

    public CompletedLessonsService(
            CompletedLessonRepository completedLessonRepository, 
            LessonService lessonService, 
            @Lazy EnrollmentService enrollmentService) {
        this.completedLessonRepository = completedLessonRepository;
        this.lessonService = lessonService;
        this.enrollmentService = enrollmentService;
    }

    public CompletedLessonResponseDTO completeLesson(EnrollmentRequestDTO enrollment, Long lessonId){
        CompletedLesson completedLesson = CompletedLesson.builder()
            .enrollment(enrollmentService.toEntity(enrollment))
            .lesson(lessonService.findById(lessonId))
            .build();

        return toResponseDTO(completedLessonRepository.save(completedLesson));
    }

    public Double calculateOverallProgress(Enrollment enrollment){
        Integer totalLessons = lessonService.countLessonsInCourse(enrollment.getCourse().getId());
        Integer completedLessons = countByEnrollment(enrollment);

        if (totalLessons == 0){
            return 0.0;
        }

        return (double) completedLessons / totalLessons;
    }

    public Integer countByEnrollment(Enrollment enrollment) {
        return completedLessonRepository.countByEnrollment(enrollment);
    }

    public Set<CompletedLessonResponseDTO> findByEnrollment(Enrollment enrollment) {
        return toResponseDTOs(completedLessonRepository.findByEnrollment(enrollment));
    }

    public Set<CompletedLessonResponseDTO> toResponseDTOs(List<CompletedLesson> completedLessons){
        return completedLessons.stream().map(this::toResponseDTO).collect(Collectors.toSet());
    }

    public CompletedLessonResponseDTO toResponseDTO(CompletedLesson completedLesson){
        return new CompletedLessonResponseDTO(
            completedLesson.getId(),
            lessonService.toResponseDTO(completedLesson.getLesson()),
            completedLesson.getCompletionDate().toString()
        );
    }
}