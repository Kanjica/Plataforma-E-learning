package com.lp3.elearning.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.ReviewRequestDTO;
import com.lp3.elearning.dto.ReviewResponseDTO;
import com.lp3.elearning.service.ReviewService;

import jakarta.validation.Valid;

@RestController

@RequestMapping("/courses/{courseId}/reviews") 
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> create(
            @PathVariable Long courseId, // ID da URL
            @RequestBody @Valid ReviewRequestDTO request) {
        
        // Passa o ID e o corpo para o serviço
        ReviewResponseDTO response = reviewService.createReview(courseId, request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> list(@PathVariable Long courseId) {
        return ResponseEntity.ok(reviewService.listByCourse(courseId));
    }
}