package com.lp3.elearning.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.dto.course.ModuleReorderRequestDTO;
import com.lp3.elearning.dto.course.ModuleRequestDTO;
import com.lp3.elearning.dto.course.ModuleResponseDTO;
import com.lp3.elearning.service.ModuleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/courses/{courseId}/modules")
@Tag(name = "Módulos", description = "Gerencia os módulos de um curso")
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService){
        this.moduleService = moduleService;
    }

    @Operation(summary = "Criar Módulo", description = "Adiciona um módulo ao curso")
    @PostMapping 
    public ResponseEntity<APIResponse<ModuleResponseDTO>> create(
        @PathVariable Long courseId, @RequestBody ModuleRequestDTO request){

        var createdModule = moduleService.create(request, courseId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdModule.id())
            .toUri();
        return ResponseEntity.created(location).body(APIResponse.success(createdModule));
    }

    @Operation(summary = "Buscar Módulo por ID")
    @GetMapping("/{moduleId}")
    public ResponseEntity<APIResponse<ModuleResponseDTO>> getById(@PathVariable Long courseId, @PathVariable Long moduleId){
        return ResponseEntity.ok(APIResponse.success(moduleService.getById(moduleId, courseId)));
    }

    @Operation(summary = "Listar Módulos do Curso")
    @GetMapping
    public ResponseEntity<APIResponse<List<ModuleResponseDTO>>> getAllByCourseId(@PathVariable Long courseId){
        return ResponseEntity.ok(APIResponse.success(moduleService.getAllByCourseId(courseId)));
    }
    
    @Operation(summary = "Reordenar Módulos", description = "Altera a ordem de exibição dos módulos")
    @PutMapping("/reorder")
    public ResponseEntity<APIResponse<List<ModuleResponseDTO>>> reorderModules(
        @PathVariable Long courseId, 
        @RequestBody List<ModuleReorderRequestDTO> requests){
        return ResponseEntity.ok(APIResponse.success(moduleService.reorder(courseId, requests)));
    }
}