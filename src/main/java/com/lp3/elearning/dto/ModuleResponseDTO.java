package com.lp3.elearning.dto;

import java.util.Set;

public record ModuleResponseDTO(
    Long id,
    String title,
    String description,
    Integer moduleOrder,
    Long courseId,
    String courseTitle,
    Set<LessonResponseDTO> lessons
) {
    
}
