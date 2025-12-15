package com.lp3.elearning.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewRequestDTO(
    @NotNull(message = "A nota é obrigatória")
    @Min(1) @Max(5)
    Integer rating,

    @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
    String comment
) {}