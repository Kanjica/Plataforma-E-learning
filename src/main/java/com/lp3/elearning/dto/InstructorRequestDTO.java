package com.lp3.elearning.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Cadastrar Instrutor (Admin)")
public record InstructorRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    @Size(max=100)
    String name,

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    String email,

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6)
    String password
) {}