package com.lp3.elearning.dto.course;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados da Aula")
public record LessonRequestDTO(
    @Schema(description = "Título da aula", example = "Instalando o VS Code")
    @NotBlank(message = "O título da aula é obrigatório")
    String title,

    @Schema(description = "Conteúdo em texto/HTML", example = "<p>Passo a passo...</p>")
    @NotBlank(message = "O conteúdo é obrigatório")
    String content,

    @Schema(description = "Ordem da aula no módulo", example = "1")
    @NotNull(message = "A ordem é obrigatória")
    @Min(1)
    Integer lessonOrder,

    @Schema(description = "Link do vídeo (YouTube/Vimeo)", example = "https://youtube.com/...")
    @NotBlank(message = "A URL do vídeo é obrigatória")
    String videoUrl
) {}