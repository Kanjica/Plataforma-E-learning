package com.lp3.elearning.dto;

import java.time.LocalDateTime;

public record ReviewResponseDTO(
    Long id,
    String studentName,
    Integer rating,
    String comment,
    LocalDateTime reviewDate
) {}