package com.lp3.elearning.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.dto.course.LessonReorderRequestDTO;
import com.lp3.elearning.dto.course.LessonRequestDTO;
import com.lp3.elearning.dto.course.LessonResponseDTO;
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
    @PostMapping
    public ResponseEntity<APIResponse<LessonResponseDTO>> createLesson(
        @PathVariable Long courseId,
        @PathVariable Long moduleId,
        @RequestBody LessonRequestDTO lessonRequest){

        var createdLesson = lessonService.create(lessonRequest, moduleId, courseId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdLesson.id())
            .toUri();
            
        return ResponseEntity.created(location).body(APIResponse.success(createdLesson));
    }

    @Operation(summary = "Buscar Aula", description = "Busca detalhes da aula e valida acesso do aluno")
    @GetMapping("/{lessonId}")
    public ResponseEntity<APIResponse<LessonResponseDTO>> getById(
        @PathVariable Long courseId,
        @PathVariable Long moduleId,
        @PathVariable Long lessonId,
        @AuthenticationPrincipal Student student ){
        return ResponseEntity.ok(APIResponse.success(lessonService.getLessonByIdForUser(lessonId, student.getId(), courseId)));
    }

    @Operation(summary = "Buscar por Ordem", description = "Navegação sequencial (ex: Aula 1, Aula 2)")
    @GetMapping("/order") 
    public ResponseEntity<APIResponse<LessonResponseDTO>> getByOrder(
        @PathVariable Long courseId,
        @PathVariable Long moduleId,
        @RequestParam Integer order,
        @AuthenticationPrincipal Student student) {
        return ResponseEntity.ok(APIResponse.success(lessonService.getByLessonOrder(moduleId, order, student.getId(), courseId)));
    }

    @Operation(summary = "Listar Aulas do Módulo")
    @GetMapping
    public ResponseEntity<APIResponse<List<LessonResponseDTO>>> getAllByModuleId(
        @PathVariable Long courseId,
        @PathVariable Long moduleId){
        return ResponseEntity.ok(APIResponse.success(lessonService.getAllByModuleId(moduleId)));
    }

    @Operation(summary = "Atualizar Aula")
    @PutMapping("/{lessonId}")
    public ResponseEntity<APIResponse<LessonResponseDTO>> updateLesson(
        @PathVariable Long courseId,
        @PathVariable Long moduleId,
        @PathVariable Long lessonId,
        @RequestBody LessonRequestDTO lessonRequest) {
        return ResponseEntity.ok(APIResponse.success(lessonService.update(lessonId, moduleId, lessonRequest)));
    }

    @Operation(summary = "Deletar Aula")
    @DeleteMapping("/{lessonId}")
    public ResponseEntity<APIResponse<Void>> deleteLesson(
        @PathVariable Long courseId,
        @PathVariable Long moduleId,
        @PathVariable Long lessonId) {
        lessonService.delete(lessonId, moduleId);
        return ResponseEntity.ok(APIResponse.success(null));
    }

    @Operation(summary = "Reordenar Aulas")
    @PutMapping("/reorder")
    public ResponseEntity<APIResponse<List<LessonResponseDTO>>> reorderLessons(
        @PathVariable Long courseId, 
        @PathVariable Long moduleId,
        @RequestBody List<LessonReorderRequestDTO> requests){
        return ResponseEntity.ok(APIResponse.success(lessonService.reorder(moduleId, requests)));
    }
}