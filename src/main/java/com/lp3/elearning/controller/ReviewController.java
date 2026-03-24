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

import com.lp3.elearning.dto.forum.ReviewRequestDTO;
import com.lp3.elearning.dto.forum.ReviewResponseDTO;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/courses/{courseId}/reviews") 
@Tag(name = "Avaliações", description = "Notas e comentários dos cursos")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    
    @Operation(summary = "Criar Avaliação")
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> create(
            @PathVariable Long courseId,
            @RequestBody @Valid ReviewRequestDTO request,
            @AuthenticationPrincipal Student student) {
        
        ReviewResponseDTO response = reviewService.createReview(courseId, request, student);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Listar Avaliações do Curso")
    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> list(@PathVariable Long courseId) {
        return ResponseEntity.ok(reviewService.listByCourse(courseId));
    }
    
    @Operation(summary = "Atualizar Avaliação")
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> update(
            @PathVariable Long courseId,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewRequestDTO request,
            @AuthenticationPrincipal Student student) {
        
        return ResponseEntity.ok(reviewService.updateReview(courseId, reviewId, request, student));
    }

    @Operation(summary = "Deletar Avaliação")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long courseId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal Student student) {
        
        reviewService.deleteReview(courseId, reviewId, student);
        return ResponseEntity.noContent().build();
    }
}