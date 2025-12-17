package com.lp3.elearning.dto;

public record LessonRequestDTO(
    String title,
    String content,
    Integer lessonOrder,
    String videoUrl
) {}
