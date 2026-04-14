package com.lp3.elearning.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.enrollment.CompletedLessonResponseDTO;
import com.lp3.elearning.entities.CompletedLesson;
import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.entities.Lesson;
import com.lp3.elearning.mapper.CompletedLessonMapper;
import com.lp3.elearning.repository.CompletedLessonRepository;

@Service 
@RequiredArgsConstructor
public class CompletedLessonsService {

    private final CompletedLessonRepository completedLessonRepository;
    private final CompletedLessonMapper completedLessonMapper;

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

    public CompletedLesson saveCompletion(Enrollment enrollment, Lesson lesson){
        CompletedLesson completedLesson = CompletedLesson.builder()
            .enrollment(enrollment) 
            .lesson(lesson)
            .completionDate(LocalDateTime.now())
            .build();
        return completedLessonRepository.save(completedLesson);
    }
}