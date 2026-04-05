package com.lp3.elearning.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.dto.course.LessonReorderRequestDTO;
import com.lp3.elearning.dto.course.LessonRequestDTO;
import com.lp3.elearning.dto.course.LessonResponseDTO;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.security.anottation.CurrentUser;
import com.lp3.elearning.service.LessonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/lessons")
@Tag(name = "Aulas", description = "Gerenciamento de conteúdos e navegação de aulas")
public class LessonController {
    
    private final LessonService lessonService;

    public LessonController(LessonService lessonService){
        this.lessonService = lessonService;
    }

    // --- OPERAÇÕES POR ID ÚNICO (ROTAS CURTAS) ---

    @Operation(summary = "Buscar Aula", description = "Busca detalhes da aula e valida acesso do aluno")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<LessonResponseDTO>> getById(
        @PathVariable Long id,
        @CurrentUser User user) { 
        // O Service deve validar se o aluno tem matrícula no curso desta aula
        return ResponseEntity.ok(APIResponse.success(lessonService.getLessonByIdForUser(id, user.getId())));
    }

    @Operation(summary = "Atualizar Aula")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.isInstructorOfLesson(#id, #user.id)")
    public ResponseEntity<APIResponse<LessonResponseDTO>> update(
        @PathVariable Long id, 
        @RequestBody @Valid LessonRequestDTO lessonRequest,
        @CurrentUser User user) {
        return ResponseEntity.ok(APIResponse.success(lessonService.update(id, lessonRequest)));
    }

    @Operation(summary = "Deletar Aula")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.isInstructorOfLesson(#id, #user.id)")
    public ResponseEntity<APIResponse<Void>> delete(@PathVariable Long id) {
        lessonService.delete(id);
        return ResponseEntity.ok(APIResponse.success(null));
    }

    @Operation(summary = "Listar Aulas do Módulo")
    @GetMapping("/module/{moduleId}")
    public ResponseEntity<APIResponse<List<LessonResponseDTO>>> getAllByModule(@PathVariable Long moduleId){
        return ResponseEntity.ok(APIResponse.success(lessonService.getAllByModuleId(moduleId)));
    }

    @Operation(summary = "Criar Aula no Módulo")
    @PostMapping("/module/{moduleId}")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.isInstructorOfModule(#moduleId, #user.id)")
    public ResponseEntity<APIResponse<LessonResponseDTO>> create(
        @PathVariable Long moduleId,
        @RequestBody @Valid LessonRequestDTO lessonRequest,
        @CurrentUser User user){

        var createdLesson = lessonService.create(lessonRequest, moduleId);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/lessons/{id}")
            .buildAndExpand(createdLesson.id())
            .toUri();
            
        return ResponseEntity.created(location).body(APIResponse.success(createdLesson));
    }

    @Operation(summary = "Reordenar Aulas", description = "Altera a ordem de exibição das aulas dentro de um módulo")
    @PutMapping("/module/{moduleId}/reorder")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.isInstructorOfModule(#moduleId, #user.id)")
    public ResponseEntity<APIResponse<List<LessonResponseDTO>>> reorder(
        @PathVariable Long moduleId,
        @RequestBody @Valid List<LessonReorderRequestDTO> requests){
        return ResponseEntity.ok(APIResponse.success(lessonService.reorder(moduleId, requests)));
    }

    @Operation(summary = "Buscar por Ordem", description = "Navegação sequencial (ex: buscar a aula 2 do módulo X)")
    @GetMapping("/module/{moduleId}/order/{order}") 
    public ResponseEntity<APIResponse<LessonResponseDTO>> getByOrder(
        @PathVariable Long moduleId,
        @PathVariable Integer order,
        @CurrentUser User user) {
        return ResponseEntity.ok(APIResponse.success(lessonService.getByLessonOrder(order, moduleId,user.getId())));
    }
}