package com.lp3.elearning.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InstructorRequestDTO(
    @NotBlank(message = "Nome do usuario não pode estar vazio")
    @Size(max=100)
    String name,

    @Email
    @NotBlank(message = "Email do usuario não pode estar vazio ")
    String email,

    @NotBlank(message = "Senha do usuario não pode estar vazia")
    String password
) {
    
}
