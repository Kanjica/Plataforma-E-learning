package com.lp3.elearning.dto;

import jakarta.validation.constraints.NotNull;

public record EnrollmentRequestDTO(
    @NotNull
    Long studentId,
    @NotNull
    Long courseId
) {}
