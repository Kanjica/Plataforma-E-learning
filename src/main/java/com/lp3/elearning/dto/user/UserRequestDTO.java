package com.lp3.elearning.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualização de perfil")
public record UserRequestDTO(
    @Schema(description = "Nome completo", example = "Maria Souza")
    @NotBlank(message = "Nome é obrigatório") 
    String name,

    @Schema(description = "E-mail", example = "maria@email.com")
    @NotBlank(message = "Email é obrigatório") 
    @Email(message = "Email inválido") 
    String email,

    @Schema(description = "Nova senha (opcional para update, obrigatório para create)", example = "novaSenha123")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres") 
    String password,

    @Schema(description = "Role (Geralmente não editável pelo próprio usuário)", hidden = true)
    String role
) {}