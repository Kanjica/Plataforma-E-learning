package com.lp3.elearning.dto;

import java.math.BigDecimal;
import java.util.Set;

public record CourseResponseDTO(
    long id,
    String title,
    String description,
    Integer workload,
    Set<CategoryResponseDTO> categories,
    Set<InstructorResponseDTO> instructors,
    String imageUrl,
    Set<ModuleResponseDTO> modules,
    BigDecimal price,
    BigDecimal oldPrice,
    Boolean isBestSeller
) {
    
}
