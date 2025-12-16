package com.lp3.elearning.dto;

public record CompletedLessonResponseDTO(
    Long id,
    LessonResponseDTO lesson,
    String completionDate,
    Double overallProgress
) {}
