package com.lp3.elearning.dto.enrollment;

import java.util.Set;

import com.lp3.elearning.dto.user.StudentResponseDTO;
import com.lp3.elearning.entities.StatusEnrollment;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalhes da matrícula")
public record EnrollmentResponseDTO(
    Long id,
    StudentResponseDTO student,
    Long courseId,
    String courseTitle,
    
    @Schema(description = "Progresso geral (0.0 a 1.0)", example = "0.75")
    Double overallProgress,
    
    @Schema(description = "Status da matrícula", example = "IN_PROGRESS")
    StatusEnrollment status,
    
    Set<CompletedLessonResponseDTO> completedLessons
) { }