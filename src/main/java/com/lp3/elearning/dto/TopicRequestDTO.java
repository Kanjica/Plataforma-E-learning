package com.lp3.elearning.dto;

public record TopicRequestDTO(
    String title,
    String content,
    Long courseId,
    Long userId
) {}
