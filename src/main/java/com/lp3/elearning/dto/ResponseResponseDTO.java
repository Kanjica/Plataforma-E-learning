package com.lp3.elearning.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResponseResponseDTO(
    Long id,
    String content,
    LocalDateTime creationDate,
    UserResponseDTO user,
    Long topicId, 
    Long responseParentId, 
    @Schema(description = "Respostas filhas (Threads)")
    List<ResponseResponseDTO> childResponses 
) {}