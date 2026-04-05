package com.lp3.elearning.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.dto.course.ModuleReorderRequestDTO;
import com.lp3.elearning.dto.course.ModuleRequestDTO;
import com.lp3.elearning.dto.course.ModuleResponseDTO;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.security.anottation.CurrentUser;
import com.lp3.elearning.service.ModuleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/modules")
@Tag(name = "Módulos", description = "Gerenciamento de módulos dos cursos")
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService){
        this.moduleService = moduleService;
    }


    @Operation(summary = "Buscar Módulo por ID")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<ModuleResponseDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(APIResponse.success(moduleService.getById(id)));
    }

    @Operation(summary = "Atualizar Módulo")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.isInstructorOfModule(#id, #user.id)")
    public ResponseEntity<APIResponse<ModuleResponseDTO>> update(
        @PathVariable Long id,
        @RequestBody @Valid ModuleRequestDTO request,
        @CurrentUser User user) {
        return ResponseEntity.ok(APIResponse.success(moduleService.update(id, request)));
    }

    @Operation(summary = "Deletar Módulo")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.isInstructorOfModule(#id, #user.id)")
    public ResponseEntity<APIResponse<Void>> delete(@PathVariable Long id) {
        moduleService.delete(id);
        return ResponseEntity.ok(APIResponse.success(null));
    }

    @Operation(summary = "Listar Módulos do Curso")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<APIResponse<List<ModuleResponseDTO>>> getAllByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(APIResponse.success(moduleService.getAllByCourseId(courseId)));
    }

    @Operation(summary = "Criar Módulo", description = "Adiciona um módulo a um curso específico")
    @PostMapping("/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.isInstructorOfCourse(#courseId, #user.id)")
    public ResponseEntity<APIResponse<ModuleResponseDTO>> create(
        @PathVariable Long courseId, 
        @RequestBody @Valid ModuleRequestDTO request,
        @CurrentUser User user) {

        var createdModule = moduleService.create(request, courseId);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/modules/{id}")
            .buildAndExpand(createdModule.id())
            .toUri();
            
        return ResponseEntity.created(location).body(APIResponse.success(createdModule));
    }

    @Operation(summary = "Reordenar Módulos", description = "Altera a ordem de exibição dos módulos de um curso")
    @PutMapping("/course/{courseId}/reorder")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.isInstructorOfCourse(#courseId, #user.id)")
    public ResponseEntity<APIResponse<List<ModuleResponseDTO>>> reorder(
        @PathVariable Long courseId, 
        @RequestBody @Valid List<ModuleReorderRequestDTO> requests,
        @CurrentUser User user) {
        return ResponseEntity.ok(APIResponse.success(moduleService.reorder(courseId, requests)));
    }
}