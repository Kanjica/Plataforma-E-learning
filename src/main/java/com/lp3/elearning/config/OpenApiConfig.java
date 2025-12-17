// Exemplo de classe de configuração OpenAPI
package com.lp3.elearning.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // Nome para referenciar o esquema de segurança
    private static final String SCHEME_NAME = "bearerAuth"; 

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("E-Learning API").version("v1")) // Informações básicas
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME)) // Adiciona o requisito de segurança global
                .components(new Components()
                        .addSecuritySchemes(SCHEME_NAME, createSecurityScheme()) // Define o esquema de segurança
                );
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name(SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP) // Tipo de esquema HTTP
                .scheme("bearer") // Especifica o uso de "Bearer"
                .bearerFormat("JWT") // O formato esperado é JWT
                .in(SecurityScheme.In.HEADER) // O token será enviado no cabeçalho
                .description("Insira o token JWT que você recebeu do endpoint de login. Ex: 'Bearer seu_token_aqui'"); // Descrição
    }
}