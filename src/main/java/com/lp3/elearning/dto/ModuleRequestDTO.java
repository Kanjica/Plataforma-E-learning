package com.lp3.elearning.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ModuleRequestDTO(
    @NotBlank(message = "O título do módulo não pode ser vazio.")
    @Size(min = 5, max = 100)
    String title, 

    @Size(max = 500)
    String description,

    @NotNull
    @Min(1)
    Integer moduleOrder
) {
    
}
