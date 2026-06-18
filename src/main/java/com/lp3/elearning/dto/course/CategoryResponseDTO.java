package com.lp3.elearning.dto.course;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

@Schema(description = "Categoria de curso")
public record CategoryResponseDTO(
    Long id, 
    @Schema(example = "Programação Backend")
    String name
) implements Serializable{}