package com.lp3.elearning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.dto.enrollment.CompletedLessonResponseDTO;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.security.anottation.CurrentUser;
// import com.lp3.elearning.service.CompletedLessonsService;
import com.lp3.elearning.service.LearningProgressService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/completed")
@Tag(name = "Progresso de Aulas", description = "Marcação de aulas como concluídas")
@RequiredArgsConstructor
public class CompletedLessonController {

    // private final CompletedLessonsService completedLessonsService;
    private final LearningProgressService learningProgressService;

    @Operation(summary = "Concluir Aula", description = "Marca a aula como assistida e atualiza o progresso")
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<APIResponse<CompletedLessonResponseDTO>> completeLesson(
            @PathVariable Long courseId, 
            @PathVariable Long lessonId, 
            @CurrentUser User user
    ){
        return ResponseEntity.ok(APIResponse.success(learningProgressService.completeLesson(user.getId(), courseId, lessonId)));
    }
}