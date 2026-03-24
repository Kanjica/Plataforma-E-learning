package com.lp3.elearning.dto.course;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LessonReorderRequestDTO(
    @NotNull Long lessonId,
    @Schema(description = "Nova posição na sequência")
    @NotNull @Min(1) Integer newOrder 
) { }