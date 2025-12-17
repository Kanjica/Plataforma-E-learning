package com.lp3.elearning.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component 
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // Define o código de status HTTP
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        
        // Define o tipo de conteúdo como JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Cria o corpo da resposta em JSON
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", "Acesso negado: Você precisa estar autenticado para acessar este recurso.");
        body.put("path", request.getServletPath());

        // Converte o Map para JSON e escreve na resposta
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}