package com.lp3.elearning.dto;

public record LoginResponseDTO(
    String token,
    String role,
    Long userId,
    String name
) {}
