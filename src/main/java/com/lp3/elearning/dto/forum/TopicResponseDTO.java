package com.lp3.elearning.dto.forum;

import com.lp3.elearning.dto.user.UserResponseDTO;

import java.time.LocalDateTime;

public record TopicResponseDTO(
    Long id,
    String title,
    String content,
    LocalDateTime creationDate,
    Long courseId,
    String courseTitle,
    UserResponseDTO user,
    Integer responseCount
) {}
