package com.lp3.elearning.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @PostMapping // ANTES: /create (Removido)
    public ResponseEntity<ModuleResponseDTO> create(@PathVariable Long courseId, @RequestBody ModuleRequestDTO request){
        return ResponseEntity.ok(moduleService.create(request, courseId));
    }

    @Operation(summary = "Buscar Módulo por ID")
    @GetMapping("/{moduleId}")
    public ResponseEntity<ModuleResponseDTO> getById(@PathVariable Long courseId, @PathVariable Long moduleId){
        return ResponseEntity.ok(moduleService.getById(moduleId, courseId));
    }

    @Operation(summary = "Listar Módulos do Curso")
    @GetMapping
    public ResponseEntity<List<ModuleResponseDTO>> getAllByCourseId(@PathVariable Long courseId){
        return ResponseEntity.ok(moduleService.getAllByCourseId(courseId));
    }
    
    @Operation(summary = "Reordenar Módulos", description = "Altera a ordem de exibição dos módulos")
    @PutMapping("/reorder")
    public ResponseEntity<List<ModuleResponseDTO>> reorderModules(
        @PathVariable Long courseId, 
        @RequestBody List<ModuleReorderRequestDTO> requests){
        return ResponseEntity.ok(moduleService.reorder(courseId, requests));
    }
}