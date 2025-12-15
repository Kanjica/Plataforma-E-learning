package com.lp3.elearning.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ModuleReorderRequestDTO(
    @NotNull Long moduleId, 
    @NotNull @Min(1) Integer newOrder 
) { }