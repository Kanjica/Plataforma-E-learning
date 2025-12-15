package com.lp3.elearning.dto;

public record LessonResponseDTO(
    Long id,
    String title,
    String content,
    Integer order,
    String videoUrl,
    Long moduleId,
    String moduleTitle,
    Long courseId,
    String courseTitle
) { }
