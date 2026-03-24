package com.lp3.elearning.dto.forum;

import java.time.LocalDateTime;
import java.util.List;

import com.lp3.elearning.dto.user.UserResponseDTO;
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