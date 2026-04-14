package com.lp3.elearning.dto.enrollment;

public record CompletedLessonResponseDTO(
        Long id, 
        Long lessonId, 
        String completionDate,
        Double overallProgress
) {}