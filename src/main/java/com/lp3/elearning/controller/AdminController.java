package com.lp3.elearning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.auth.AdminRegisterDTO;
import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.service.AdminService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Operações relacionadas a administradores")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<APIResponse<String>> register(@RequestBody @Valid AdminRegisterDTO data) {
        adminService.createAdmin(data);
        return ResponseEntity.ok(APIResponse.success("Novo administrador registrado com sucesso!"));
    }
}
