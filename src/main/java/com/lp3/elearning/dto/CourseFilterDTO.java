package com.lp3.elearning.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;

public record CourseFilterDTO(
    String title,
    Set<Long> categoryIds
) {}
