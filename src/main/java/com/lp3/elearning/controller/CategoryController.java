package com.lp3.elearning.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(
        @RequestBody @Valid CategoryRequestDTO dto,
        @AuthenticationPrincipal Instructor instructor) {
        return ResponseEntity.ok(categoriesService.createCategory(dto));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAll(@AuthenticationPrincipal Instructor instructor) {
        return ResponseEntity.ok(categoriesService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable Long id, @AuthenticationPrincipal Instructor instructor) {
        return ResponseEntity.ok(categoriesService.findById(id));
    }
}