package com.lp3.elearning.dto;

import java.util.Set;

public record CourseFilterDTO(
    String title,
    Set<Long> categoryIds
) {}
