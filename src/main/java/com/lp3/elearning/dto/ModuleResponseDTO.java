package com.lp3.elearning.dto;

import java.util.Set;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do Módulo")
public record ModuleResponseDTO(
    Long id,
    String title,
    String description,
    
    @Schema(description = "Ordem de exibição do módulo")
    Integer moduleOrder,
    
    Long courseId,
    String courseTitle,
    
    @Schema(description = "Lista de aulas deste módulo")
    Set<LessonResponseDTO> lessons
) {}