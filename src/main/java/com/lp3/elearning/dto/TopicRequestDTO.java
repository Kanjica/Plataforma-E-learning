package com.lp3.elearning.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Criação de novo tópico no fórum")
public record TopicRequestDTO(
    @Schema(description = "Título da dúvida/discussão", example = "Erro ao compilar projeto")
    @NotBlank(message = "O título é obrigatório")
    String title,

    @Schema(description = "Detalhes da dúvida", example = "Estou recebendo NullPointerException na linha 10...")
    @NotBlank(message = "O conteúdo é obrigatório")
    String content,

    @Schema(description = "ID do curso relacionado", example = "1")
    @NotNull(message = "O ID do curso é obrigatório")
    Long courseId
) {}