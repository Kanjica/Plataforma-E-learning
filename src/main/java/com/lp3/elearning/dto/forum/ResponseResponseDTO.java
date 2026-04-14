package com.lp3.elearning.dto.forum;

import java.time.LocalDateTime;

import com.lp3.elearning.dto.user.UserResponseDTO;

public record ResponseResponseDTO(
    Long id,
    String content,
    LocalDateTime creationDate,
    UserResponseDTO user,
    Long topicId, 
    Long responseParentId, 
    boolean hasChildren, // indica se existem respostas abaixo desta
    Integer childrenCount
) {}