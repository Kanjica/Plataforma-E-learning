package com.lp3.elearning.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record TopicResponseDTO(
    Long id,
    String title,
    String content,
    LocalDateTime creationDate,
    CourseResponseDTO course,
    UserResponseDTO user,
    Set<ResponseResponseDTO> responses
) {}
