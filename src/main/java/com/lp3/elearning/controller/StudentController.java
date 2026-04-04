package com.lp3.elearning.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.auth.StudentRegisterDTO;
import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.dto.enrollment.EnrollmentResponseDTO;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.service.EnrollmentService;
import com.lp3.elearning.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")
@Tag(name = "Área do Aluno", description = "Dashboard e dados do aluno")
public class StudentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;

    public StudentController(EnrollmentService enrollmentService, StudentService studentService) { 
        this.enrollmentService = enrollmentService; 
        this.studentService = studentService;
    }

    @Operation(summary = "Registrar Aluno", description = "Registra um novo aluno na plataforma")
    @PostMapping("/register")
    public ResponseEntity<APIResponse<String>> register(@RequestBody @Valid StudentRegisterDTO data){
        studentService.createStudent(data);
        return ResponseEntity.ok(APIResponse.success("Aluno registrado com sucesso"));
    }

    @Operation(summary = "Meu Dashboard", description = "Retorna cursos em andamento do aluno logado")
    @GetMapping("/dashboard")
    public ResponseEntity<APIResponse<List<EnrollmentResponseDTO>>> getMyDashboard(
        @AuthenticationPrincipal Student student) {
        return ResponseEntity.ok(APIResponse.success(enrollmentService.getMyEnrollments(student.getId())));
    }
}