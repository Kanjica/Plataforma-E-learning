package com.lp3.elearning.dto;
import com.lp3.elearning.entities.UserRole;
public record UserResponseDTO(Long id, String name, String email, UserRole role) { }