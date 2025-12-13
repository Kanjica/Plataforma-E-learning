package com.lp3.elearning.dto;

import java.util.Set;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CourseRequestDTO(
    @NotBlank(message = "O título do curso não pode ser vazio.")
    @Size(min = 5, max = 150) 
    String title, 

    @NotBlank(message = "A descrição do curso não pode ser vazia.")
    @Size(min = 20)
    String description, 

    @NotNull(message = "O campo é obrigatório.")
    @Min(value = 10, message = "A carga horária mínima para um curso é de 10 horas.")
    Integer workload, 
    
    Set<Long> categoryIds,
    Set<Long> instructorIds) {
    
}
