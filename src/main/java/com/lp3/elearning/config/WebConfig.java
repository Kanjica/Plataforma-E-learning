package com.lp3.elearning.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig{

    // Lê do application.properties, mas usa localhost:4200 como padrão se não encontrar
    @Value("${cors.origins:http://localhost:4200}")
    private String corsOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer(){
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**") // Aplica a todas as rotas
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Métodos permitidos
                        .allowedHeaders("*") // Permite todos os headers (Authorization, Content-Type, etc)
                        .allowedOrigins(corsOrigins) // Origens permitidas (Frontend)
                        .allowCredentials(true); // Permite envio de cookies/credenciais se necessário
            }
        };
    }
}