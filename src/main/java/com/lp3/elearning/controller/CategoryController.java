package com.lp3.elearning.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.CategoryRequestDTO;
import com.lp3.elearning.dto.CategoryResponseDTO;
import com.lp3.elearning.entities.Instructor;
import com.lp3.elearning.service.CategoriesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoriesService categoriesService;

    public CategoryController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    // [1] Criação - Restrito a Instrutores (ou Admin)
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(
        @RequestBody @Valid CategoryRequestDTO dto,
        @AuthenticationPrincipal Instructor instructor) { // Mantém a restrição aqui
        return ResponseEntity.ok(categoriesService.createCategory(dto));
    }

    // [2] Leitura - PÚBLICO (Removi o @AuthenticationPrincipal)
    // Qualquer um deve poder ver as categorias para filtrar cursos
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAll() {
        return ResponseEntity.ok(categoriesService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoriesService.findById(id));
    }

    // [3] Falta o UPDATE (Correção de nomes)
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequestDTO dto,
            @AuthenticationPrincipal Instructor instructor) {
        return ResponseEntity.ok(categoriesService.update(id, dto));
    }

    // [4] Falta o DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal Instructor instructor) {
        categoriesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}