package com.lp3.elearning.controller;

import java.net.URI;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.dto.course.CategoryRequestDTO;
import com.lp3.elearning.dto.course.CategoryResponseDTO;
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

    @Operation(summary = "Criar Categoria", description = "Requer permissão de Administrador")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<CategoryResponseDTO>> create(
        @RequestBody @Valid CategoryRequestDTO dto){

        var createdCategory = categoriesService.createCategory(dto);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdCategory.id())
            .toUri();

        return ResponseEntity.created(uri).body(APIResponse.success(createdCategory));
    }

    @Operation(summary = "Listar todas as categorias", description = "Acesso público")
    @GetMapping
    public ResponseEntity<APIResponse<Page<CategoryResponseDTO>>> findAll(
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable
    ) {
        return ResponseEntity.ok(APIResponse.success(categoriesService.findAllPaged(pageable)));
    }

    @Operation(summary = "Buscar categoria por ID")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<CategoryResponseDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(APIResponse.success(categoriesService.findById(id)));
    }

    @Operation(summary = "Atualizar Categoria")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<CategoryResponseDTO>> update(
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequestDTO dto
    ) {
        return ResponseEntity.ok(APIResponse.success(categoriesService.update(id, dto)));
    }

    @Operation(summary = "Deletar Categoria")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Void>> delete(@PathVariable Long id) {
        categoriesService.delete(id);
        return ResponseEntity.ok(APIResponse.success(null));
    }
}