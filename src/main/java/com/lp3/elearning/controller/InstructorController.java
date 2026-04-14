package com.lp3.elearning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.auth.InstructorRegisterDTO;
import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.dto.user.InstructorResponseDTO;
import com.lp3.elearning.service.InstructorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/instructors")
@Tag(name = "Instrutores", description = "Visualização pública de perfis de instrutores")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @Operation(summary = "Registrar Instrutor", description = "Permite que um novo instrutor se registre na plataforma")
    @PostMapping("/register")
    public ResponseEntity<APIResponse<String>> register(@RequestBody @Valid InstructorRegisterDTO data){
        instructorService.createInstructor(data);
        return ResponseEntity.ok(APIResponse.success("Instrutor registrado com sucesso"));
    }

    @Operation(summary = "Perfil do Instrutor", description = "Busca detalhes públicos de um instrutor")
    @GetMapping("/{instructorId}")
    public ResponseEntity<APIResponse<InstructorResponseDTO>> getInstructorById(@PathVariable Long instructorId) {
        return ResponseEntity.ok(APIResponse.success(instructorService.findById(instructorId)));
    }

}