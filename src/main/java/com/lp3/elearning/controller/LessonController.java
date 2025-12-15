package com.lp3.elearning.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.LessonReorderRequestDTO;
import com.lp3.elearning.dto.LessonRequestDTO;
import com.lp3.elearning.dto.LessonResponseDTO;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.service.LessonService;

@RestController
@RequestMapping("/courses/{courseId}/modules/{moduleId}/lessons")
public class LessonController {
    
    private final LessonService lessonService;

    public LessonController(LessonService lessonService){
        this.lessonService = lessonService;
    }

    @PostMapping("/create")
    public ResponseEntity<LessonResponseDTO> createLesson(
        @PathVariable Long courseId,
        @PathVariable Long moduleId,
        @RequestBody LessonRequestDTO lessonRequest){
        lessonService.create(lessonRequest);
        return ResponseEntity.ok(lessonService.create(lessonRequest));
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonResponseDTO> getById(
        @PathVariable Long courseId,
        @PathVariable Long moduleId,
        @PathVariable Long lessonId,
        // AQUI: Injeta o objeto Student autenticado
        @AuthenticationPrincipal Student student ){
        
        // Altera a chamada para o LessonService para usar o studentId e o courseId.
        // O LessonService agora fará a ponte entre Student e Enrollment.
        return ResponseEntity.ok(lessonService.getLessonByIdForUser(lessonId, student.getId(), courseId));
    }

    @GetMapping
    public ResponseEntity<List<LessonResponseDTO>> getAllByModuleId(
        @PathVariable Long courseId,
        @PathVariable Long moduleId){
        List<LessonResponseDTO> lessons = lessonService.getAllByModuleId(moduleId);
        return ResponseEntity.ok(lessons);
    }

    @PutMapping("/reorder")
    public ResponseEntity<List<LessonResponseDTO>> reorderLessons(
        @PathVariable Long courseId, 
        @PathVariable Long moduleId,
        @RequestBody List<LessonReorderRequestDTO> requests){
        
        List<LessonResponseDTO> updatedLessons = lessonService.reorder(moduleId, requests);
        return ResponseEntity.ok(updatedLessons);
    }
    
}
