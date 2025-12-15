package com.lp3.elearning.dto;

import java.util.Set;

import com.lp3.elearning.entities.StatusEnrollment;

public record EnrollmentResponseDTO(
    Long id,
    StudentResponseDTO student,
    CourseResponseDTO course,
    Double overallProgress,
    StatusEnrollment status,
    Set<CompletedLessonResponseDTO> completedLessons
) { }
