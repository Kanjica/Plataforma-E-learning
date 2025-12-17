package com.lp3.elearning.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record TopicResponseDTO(
    Long id,
    String title,
    String content,
    LocalDateTime creationDate,
    Long courseId,
    String courseTitle,
    UserResponseDTO user,
    Set<ResponseResponseDTO> responses
) {}
