package com.lp3.elearning.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.EnrollmentResponseDTO;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.service.EnrollmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/students")
@Tag(name = "Área do Aluno", description = "Dashboard e dados do aluno")
public class StudentController {

    private final EnrollmentService enrollmentService;

    public StudentController(EnrollmentService enrollmentService) { this.enrollmentService = enrollmentService; }

    @Operation(summary = "Meu Dashboard", description = "Retorna cursos em andamento do aluno logado")
    @GetMapping("/dashboard")
    public ResponseEntity<List<EnrollmentResponseDTO>> getMyDashboard(@AuthenticationPrincipal Student student) {
        return ResponseEntity.ok(enrollmentService.getMyEnrollments(student.getId()));
    }
}