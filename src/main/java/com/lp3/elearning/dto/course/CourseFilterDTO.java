package com.lp3.elearning.dto.course;

import java.util.Set;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filtros de busca de curso")
public record CourseFilterDTO(
    @Schema(description = "Parte do título", example = "Java")
    String title,
    
    @Schema(description = "Lista de IDs de categorias")
    Set<Long> categoryIds
) {}