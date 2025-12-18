package com.lp3.elearning.dto;

import java.math.BigDecimal;
import java.util.Set;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalhes completos do curso para exibição")
public record CourseResponseDTO(
    @Schema(description = "ID do curso")
    long id,
    
    @Schema(description = "Título", example = "Java Masterclass")
    String title,
    
    @Schema(description = "Descrição curta")
    String description,
    
    @Schema(description = "Carga horária total")
    Integer workload,
    
    @Schema(description = "Categorias associadas")
    Set<CategoryResponseDTO> categories,
    
    @Schema(description = "Instrutores do curso")
    Set<InstructorResponseDTO> instructors,
    
    @Schema(description = "URL da imagem de capa")
    String imageUrl,
    
    @Schema(description = "Módulos do curso (pode ser nulo em listagens compactas)")
    Set<ModuleResponseDTO> modules,
    
    @Schema(description = "Preço atual")
    BigDecimal price,
    
    @Schema(description = "Preço original (riscado)")
    BigDecimal oldPrice,
    
    @Schema(description = "Flag de destaque")
    Boolean isBestSeller
) {}