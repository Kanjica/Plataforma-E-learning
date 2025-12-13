package com.lp3.elearning.dto;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthenticationDTO(
    @NotBlank String login, 
    @NotBlank String password
    ) {
}
