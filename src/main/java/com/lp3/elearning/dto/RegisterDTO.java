package com.lp3.elearning.dto;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
    @NotNull @NotBlank(message = "Nome é obrigatório") @NonNull String name,
    @Email @NotBlank(message = "Email inválido") @NonNull String login,
    @NotNull @NotBlank(message = "Senha é obrigatória") @NonNull @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres") String password,
    @NotNull @NotBlank(message = "Role é obrigatória") @NonNull String role
    ) {
}
