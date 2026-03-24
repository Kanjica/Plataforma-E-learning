package com.lp3.elearning.dto.course;

import java.math.BigDecimal;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação ou atualização de curso")
public record CourseRequestDTO(
    @Schema(description = "Título do curso", example = "Introdução ao Java")
    @NotBlank(message = "O título do curso não pode ser vazio.")
    @Size(min = 5, max = 150, message = "O título deve ter entre 5 e 150 caracteres") 
    String title, 

    @Schema(description = "Descrição detalhada", example = "Aprenda Java do zero ao avançado...")
    @NotBlank(message = "A descrição do curso não pode ser vazia.")
    @Size(min = 20, message = "A descrição deve ter no mínimo 20 caracteres")
    String description, 

    @Schema(description = "Carga horária em horas", example = "40")
    @NotNull(message = "Carga horária é obrigatória.")
    @Min(value = 1, message = "Carga horária mínima de 1 hora") // Ajustei para 1 para facilitar testes, mas pode ser 10
    Integer workload, 
    
    @Schema(description = "IDs das categorias", example = "[1, 2]")
    @NotNull(message = "O campo de categorias é obrigatório.")
    Set<Long> categoryIds,

    @Schema(description = "IDs dos instrutores", example = "[1]")
    @NotNull(message = "O campo de instrutores é obrigatório.")
    Set<Long> instructorIds,

    @Schema(description = "URL da imagem de capa", example = "https://img.com/curso-java.png")
    @NotBlank(message = "A URL da imagem é obrigatória.")
    String imageUrl,

    @Schema(description = "Preço atual", example = "29.90")
    @NotNull
    @PositiveOrZero
    BigDecimal price,

    @Schema(description = "Preço original (para promoção)", example = "59.90")
    @NotNull
    @PositiveOrZero
    BigDecimal oldPrice,

    @Schema(description = "Indica se é um best seller", example = "false")
    @NotNull
    Boolean isBestSeller
) {}