package com.lp3.elearning.dto;

public record ModuleResponseDTO(
    Long id,
    String title,
    String description,
    Integer moduleOrder,
    Long courseId,
    String courseTitle
) {
    
}
