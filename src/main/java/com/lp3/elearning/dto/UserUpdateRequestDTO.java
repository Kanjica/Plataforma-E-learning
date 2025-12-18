package com.lp3.elearning.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDTO(
    @Schema(description = "Nome completo", example = "Maria Souza")
    @NotBlank(message = "Nome é obrigatório") 
    String name,

    @Schema(description = "E-mail", example = "maria@email.com")
    @NotBlank(message = "Email é obrigatório") 
    @Email(message = "Email inválido") 
    String email

) {}