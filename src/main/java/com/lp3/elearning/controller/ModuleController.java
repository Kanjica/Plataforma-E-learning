package com.lp3.elearning.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.ModuleReorderRequestDTO;
import com.lp3.elearning.dto.ModuleRequestDTO;
import com.lp3.elearning.dto.ModuleResponseDTO;
import com.lp3.elearning.service.ModuleService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/courses/{courseId}/modules")    
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService){
        this.moduleService = moduleService;
    }

    @PostMapping("/create")
    public ResponseEntity<ModuleResponseDTO> create(@PathVariable Long courseId, @RequestBody ModuleRequestDTO request){
        return ResponseEntity.ok(moduleService.create(request, courseId));
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<ModuleResponseDTO> getById(@PathVariable Long courseId, @PathVariable Long moduleId){
        return ResponseEntity.ok(moduleService.getById(moduleId, courseId));
    }

    @GetMapping
    public ResponseEntity<List<ModuleResponseDTO>> getAllByCourseId(@PathVariable Long courseId){
        List<ModuleResponseDTO> modules = moduleService.getAllByCourseId(courseId);
        return ResponseEntity.ok(modules);
    }
    
    @PutMapping("/reorder")
    public ResponseEntity<List<ModuleResponseDTO>> reorderModules(
        @PathVariable Long courseId, 
        @RequestBody List<ModuleReorderRequestDTO> requests){
        
        List<ModuleResponseDTO> updatedModules = moduleService.reorder(courseId, requests);
        return ResponseEntity.ok(updatedModules);
    }
    
}
