package com.lp3.elearning.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResponseRequestDTO(
    @NotBlank(message = "O conteúdo não pode estar vazio.") String content,
    @NotNull(message = "O ID do tópico é obrigatório.") Long topicId,
    Long responseParentId // Opcional: ID da resposta que está sendo respondida (reply)
) {}