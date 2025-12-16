package com.lp3.elearning.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseResponseDTO(
    Long id,
    String content,
    LocalDateTime creationDate,
    UserResponseDTO user,
    Long topicId, 
    Long responseParentId, 
    List<ResponseResponseDTO> childResponses // Para montar o thread
) {}