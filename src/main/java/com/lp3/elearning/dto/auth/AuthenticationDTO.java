package com.lp3.elearning.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais para login")
public record AuthenticationDTO(
    @Schema(description = "E-mail do usuário", example = "aluno@email.com")
    @NotBlank(message = "O login/email é obrigatório") 
    String login, 
    
    @Schema(description = "Senha do usuário", example = "123456")
    @NotBlank(message = "A senha é obrigatória") 
    String password
) {}