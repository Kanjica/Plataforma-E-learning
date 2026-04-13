package com.lp3.elearning.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação bem-sucedida")
public record LoginResponseDTO(
    @Schema(description = "Token JWT para acesso")
    String token,
    
    @Schema(description = "Role do usuário", example = "ROLE_STUDENT")
    String role,
    
    @Schema(description = "ID do usuário")
    Long userId,
    
    @Schema(description = "Nome do usuário")
    String username
) {}