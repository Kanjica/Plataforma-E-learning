package com.lp3.elearning.controller;

import java.net.URI;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.dto.forum.ReviewRequestDTO;
import com.lp3.elearning.dto.forum.ReviewResponseDTO;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.security.anottation.CurrentUser;
import com.lp3.elearning.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/reviews") 
@Tag(name = "Avaliações", description = "Notas e comentários dos cursos")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    
    @Operation(summary = "Criar Avaliação", description = "O aluno avalia um curso específico")
    @PostMapping("/course/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<APIResponse<ReviewResponseDTO>> create(
            @PathVariable Long courseId,
            @RequestBody @Valid ReviewRequestDTO request,
            @CurrentUser User user
    ) {
        Student student = (Student) user; 
        var createdReview = reviewService.createReview(courseId, request, student);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdReview.id())
            .toUri();
            
        return ResponseEntity.created(location).body(APIResponse.success(createdReview));
    }

    @Operation(summary = "Listar Avaliações do Curso (Paginado)")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<APIResponse<Page<ReviewResponseDTO>>> listByCourse(
            @PathVariable Long courseId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(APIResponse.success(reviewService.listByCourse(courseId, pageable)));
    }
    
    @Operation(summary = "Atualizar Avaliação")
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<ReviewResponseDTO>> update(
            @PathVariable Long id,
            @RequestBody @Valid ReviewRequestDTO request,
            @CurrentUser User user) {
        
        Student student = (Student) user;
        ReviewResponseDTO response = reviewService.updateReview(id, request, student);
        return ResponseEntity.ok(APIResponse.success(response));
    }

    @Operation(summary = "Deletar Avaliação")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Void>> delete(
            @PathVariable Long id,
            @CurrentUser User user
    ) {
        reviewService.deleteReview(id, user);
        return ResponseEntity.ok(APIResponse.success(null));
    }
}