package com.lp3.elearning.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados do Módulo")
public record ModuleRequestDTO(
    @Schema(description = "Título do módulo", example = "Configurando o Ambiente")
    @NotBlank(message = "O título do módulo é obrigatório.")
    @Size(min = 3, max = 100)
    String title, 

    @Schema(description = "Descrição do conteúdo", example = "Instalação da JDK e IDE")
    @Size(max = 500)
    String description,

    @Schema(description = "Ordem de exibição", example = "1")
    @NotNull
    @Min(1)
    Integer moduleOrder
) {}