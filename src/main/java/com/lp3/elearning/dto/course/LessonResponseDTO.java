package com.lp3.elearning.dto.course;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados da Aula")
public record LessonResponseDTO(
    Long id,
    String title,
    
    @Schema(description = "Conteúdo HTML/Texto")
    String content,
    
    @Schema(description = "Número da aula na sequência")
    Integer lessonOrder,
    
    @Schema(description = "URL do vídeo")
    String videoUrl,
    
    Long moduleId,
    String moduleTitle
) implements Serializable{}