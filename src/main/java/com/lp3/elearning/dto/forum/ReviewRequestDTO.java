package com.lp3.elearning.dto.forum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Criar ou editar avaliação")
public record ReviewRequestDTO(
    @Schema(description = "Nota de 1 a 5", example = "5")
    @NotNull(message = "A nota é obrigatória")
    @Min(1) @Max(5)
    Integer rating,

    @Schema(description = "Comentário opcional", example = "Curso excelente!")
    @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
    String comment
) {}