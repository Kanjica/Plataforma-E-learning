package com.lp3.elearning.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Enviar resposta ou comentário")
public record ResponseRequestDTO(
    @Schema(description = "Conteúdo da resposta", example = "Tente usar o comando git init.")
    @NotBlank(message = "O conteúdo não pode estar vazio.") 
    String content,
    
    @Schema(description = "ID do tópico pai")
    @NotNull(message = "O ID do tópico é obrigatório.") 
    Long topicId,
    
    @Schema(description = "ID da resposta pai (se for um reply aninhado)", nullable = true)
    Long responseParentId 
) {}