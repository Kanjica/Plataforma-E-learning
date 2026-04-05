package com.lp3.elearning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.auth.StudentRegisterDTO;
import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")
@Tag(name = "Área do Aluno", description = "Dashboard e dados do aluno")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) { 
        this.studentService = studentService;
    }

    @Operation(summary = "Registrar Aluno", description = "Registra um novo aluno na plataforma")
    @PostMapping("/register")
    public ResponseEntity<APIResponse<String>> register(@RequestBody @Valid StudentRegisterDTO data){
        studentService.createStudent(data);
        return ResponseEntity.ok(APIResponse.success("Aluno registrado com sucesso"));
    }

}