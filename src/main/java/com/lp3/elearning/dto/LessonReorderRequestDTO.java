package com.lp3.elearning.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LessonReorderRequestDTO(
    @NotNull Long lessonId,
    @NotNull @Min(1) Integer newOrder 
) { }