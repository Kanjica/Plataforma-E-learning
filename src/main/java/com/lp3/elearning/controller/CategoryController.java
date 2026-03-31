package com.lp3.elearning.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.dto.course.CategoryRequestDTO;
import com.lp3.elearning.dto.course.CategoryResponseDTO;
import com.lp3.elearning.entities.Instructor;
import com.lp3.elearning.service.CategoriesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categorias", description = "Gerenciamento de categorias para organização dos cursos")
public class CategoryController {

    private final CategoriesService categoriesService;

    public CategoryController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @Operation(summary = "Criar Categoria", description = "Requer permissão de Instrutor")
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(
        @RequestBody @Valid CategoryRequestDTO dto,
        @AuthenticationPrincipal Instructor instructor) {
        return ResponseEntity.ok(categoriesService.createCategory(dto));
    }

    @Operation(summary = "Listar todas as categorias", description = "Acesso público")
    @GetMapping
    public ResponseEntity<APIResponse<List<CategoryResponseDTO>>> findAll() {
        return ResponseEntity.ok(APIResponse.success(categoriesService.findAll()));
    }

    @Operation(summary = "Buscar categoria por ID")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<CategoryResponseDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(APIResponse.success(categoriesService.findById(id)));
    }

    @Operation(summary = "Atualizar Categoria")
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<CategoryResponseDTO>> update(
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequestDTO dto,
            @AuthenticationPrincipal Instructor instructor) {
        return ResponseEntity.ok(APIResponse.success(categoriesService.update(id, dto)));
    }

    @Operation(summary = "Deletar Categoria")
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal Instructor instructor) {
        categoriesService.delete(id);
        return ResponseEntity.ok(APIResponse.success(null));
    }
}