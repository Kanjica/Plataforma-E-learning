package com.lp3.elearning.dto.auth;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InstructorRegisterDTO(
    @NotNull @NotBlank(message = "Nome de usuário é obrigatório") @NonNull String username,
    @Email @NotBlank(message = "Email inválido") @NonNull String email,
    @NotNull @NotBlank(message = "Senha é obrigatória") @NonNull @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres") String password
    ) {
}
