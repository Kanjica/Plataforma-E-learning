package com.lp3.elearning.dto.forum;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Avaliação exibida")
public record ReviewResponseDTO(
    Long id,
    String studentName,
    Integer rating,
    String comment,
    LocalDateTime reviewDate
) {}