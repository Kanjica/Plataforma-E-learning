package com.lp3.elearning.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.LessonReorderRequestDTO;
import com.lp3.elearning.dto.LessonRequestDTO;
import com.lp3.elearning.dto.LessonResponseDTO;
import com.lp3.elearning.service.LessonService;

@RestController
@RequestMapping("/courses/{courseId}/modules/{moduleId}/lessons")
public class LessonController {
    
    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping("/create")
    public ResponseEntity<LessonResponseDTO> createLesson(
        @PathVariable Long courseId,
        @PathVariable Long moduleId,
        @RequestBody LessonRequestDTO lessonRequest) {
        lessonService.create(lessonRequest);
        return ResponseEntity.ok(lessonService.create(lessonRequest));
    }

    @PutMapping("/reorder")
    public ResponseEntity<List<LessonResponseDTO>> reorderLessons(
        @PathVariable Long courseId, // Opcional, para validação de segurança/propriedade
        @PathVariable Long moduleId,
        @RequestBody List<LessonReorderRequestDTO> requests) {
        
        List<LessonResponseDTO> updatedLessons = lessonService.reorder(moduleId, requests);
        return ResponseEntity.ok(updatedLessons);
    }
    
}
