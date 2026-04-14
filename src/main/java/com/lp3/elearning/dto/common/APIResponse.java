package com.lp3.elearning.dto.common;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record APIResponse<T>(
    boolean sucesso,
    T dados,
    String mensagem,
    Instant timestamp, 
    Object erros 
) {
    public static <T> APIResponse<T> success(T dados) {
        return new APIResponse<>(true, dados, "Sucesso", Instant.now(), null);
    }

    public static <T> APIResponse<T> success(T dados, String mensagem) {
        return new APIResponse<>(true, dados, mensagem, Instant.now(), null);
    }

    public static <T> APIResponse<T> error(String mensagem, Object detalhesErro) {
        return new APIResponse<>(false, null, mensagem, Instant.now(), detalhesErro);
    }
}