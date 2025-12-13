package com.lp3.elearning.dto;

import com.lp3.elearning.entities.RoleUsuario;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
    @NotNull @NotBlank @NonNull String name,
    @Email @NotBlank @NonNull String login,
    @NotNull @NotBlank @NonNull String password
    ) {
}
