package com.lp3.elearning.dto.course;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
    
@Schema(description = "Dados do Módulo")
public record ModuleResponseDTO(
    Long id,
    String title,
    String description,
    
    @Schema(description = "Ordem de exibição do módulo")
    Integer moduleOrder,
    
    Long courseId,
    String courseTitle
) implements Serializable{}