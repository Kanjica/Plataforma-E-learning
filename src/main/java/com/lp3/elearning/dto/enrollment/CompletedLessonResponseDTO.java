package com.lp3.elearning.dto.enrollment;

import com.lp3.elearning.dto.course.LessonResponseDTO;

public record CompletedLessonResponseDTO(
        Long id, LessonResponseDTO lesson, String completionDate, Double overallProgress
) {}