package com.lp3.elearning.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Solicitação de matrícula")
public record EnrollmentRequestDTO(
    @Schema(description = "ID do aluno (geralmente pego do token, mas útil para admins)")
    @NotNull
    Long studentId,
    
    @Schema(description = "ID do curso")
    @NotNull
    Long courseId
) {}