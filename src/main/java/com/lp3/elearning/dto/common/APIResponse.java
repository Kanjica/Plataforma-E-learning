package com.lp3.elearning.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record APIResponse<T>(
    boolean sucesso,
    T dados,
    String mensagem,
    String timestamp,
    Object erros 
) {
    
    // Sucesso padrão
    public static <T> APIResponse<T> success(T dados) {
        return new APIResponse<>(
            true, 
            dados, 
            "Operação realizada com sucesso", 
            LocalDateTime.now().toString(), 
            null
        );
    }

    // Erro padrão
    public static <T> APIResponse<T> error(String mensagem, Object detalhesErro) {
        return new APIResponse<>(
            false, 
            null, 
            mensagem, 
            LocalDateTime.now().toString(), 
            detalhesErro
        );
    }
}