package com.lp3.elearning.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Criar nova categoria")
public record CategoryRequestDTO(
    @Schema(description = "Nome único da categoria", example = "DevOps")
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres") 
    String name
) {}