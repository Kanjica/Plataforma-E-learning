package com.lp3.elearning.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.lp3.elearning.dto.LessonReorderRequestDTO;
import com.lp3.elearning.dto.LessonRequestDTO;
import com.lp3.elearning.dto.LessonResponseDTO;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.service.LessonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/courses/{courseId}/modules/{moduleId}/lessons")
@Tag(name = "Aulas", description = "Gerencia as aulas e conteúdos dos módulos")
public class LessonController {
    
    private final LessonService lessonService;

    public LessonController(LessonService lessonService){
        this.lessonService = lessonService;
    }

    @Operation(summary = "Criar Aula")
    @PostMapping // ANTES: /create (Removido)
    public ResponseEntity<LessonResponseDTO> createLesson(
        @PathVariable Long courseId,
        @PathVariable Long moduleId,
        @RequestBody LessonRequestDTO lessonRequest){
        return ResponseEntity.ok(lessonService.create(lessonRequest, moduleId, courseId));
    }

    @Operation(summary = "Buscar Aula", description = "Busca detalhes da aula e valida acesso do aluno")
    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonResponseDTO> getById(
        @PathVariable Long courseId,
        @PathVariable Long moduleId,
        @PathVariable Long lessonId,
        @AuthenticationPrincipal Student student ){
        return ResponseEntity.ok(lessonService.getLessonByIdForUser(lessonId, student.getId(), courseId));
    }

    @Operation(summary = "Buscar por Ordem", description = "Navegação sequencial (ex: Aula 1, Aula 2)")
    @GetMapping("/order") 
    public ResponseEntity<LessonResponseDTO> getByOrder(
        @PathVariable Long courseId,
        @PathVariable Long moduleId,
        @RequestParam Integer order,
        @AuthenticationPrincipal Student student) {
        return ResponseEntity.ok(lessonService.getByLessonOrder(moduleId, order, student.getId(), courseId));
    }

    @Operation(summary = "Listar Aulas do Módulo")
    @GetMapping
    public ResponseEntity<List<LessonResponseDTO>> getAllByModuleId(
        @PathVariable Long courseId,
        @PathVariable Long moduleId){
        return ResponseEntity.ok(lessonService.getAllByModuleId(moduleId));
    }

    @Operation(summary = "Atualizar Aula")
    @PutMapping("/{lessonId}")
    public ResponseEntity<LessonResponseDTO> updateLesson(
        @PathVariable Long courseId,
        @PathVariable Long moduleId,
        @PathVariable Long lessonId,
        @RequestBody LessonRequestDTO lessonRequest) {
        return ResponseEntity.ok(lessonService.update(lessonId, moduleId, lessonRequest));
    }

    @Operation(summary = "Deletar Aula")
    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> deleteLesson(
        @PathVariable Long courseId,
        @PathVariable Long moduleId,
        @PathVariable Long lessonId) {
        lessonService.delete(lessonId, moduleId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reordenar Aulas")
    @PutMapping("/reorder")
    public ResponseEntity<List<LessonResponseDTO>> reorderLessons(
        @PathVariable Long courseId, 
        @PathVariable Long moduleId,
        @RequestBody List<LessonReorderRequestDTO> requests){
        return ResponseEntity.ok(lessonService.reorder(moduleId, requests));
    }
}