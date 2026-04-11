package com.lp3.elearning.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.dto.user.UserResponseDTO;
import com.lp3.elearning.dto.user.UserUpdateRequestDTO;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.security.anottation.CurrentUser;
import com.lp3.elearning.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Gestão de perfil e administração de usuários")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @Operation(summary = "Meu Perfil", description = "Dados do usuário logado")
    @GetMapping("/me")
    public ResponseEntity<APIResponse<UserResponseDTO>> getMyProfile(@CurrentUser User user) {
        return ResponseEntity.ok(APIResponse.success(userService.findById(user.getId())));
    }

    @Operation(summary = "Atualizar Meu Perfil")
    @PutMapping("/me")
    public ResponseEntity<APIResponse<UserResponseDTO>> updateMyProfile(
            @CurrentUser User user, 
            @RequestBody @Valid UserUpdateRequestDTO request) {
        return ResponseEntity.ok(APIResponse.success(userService.updateProfile(user.getId(), request)));
    }

    @Operation(summary = "Listar Usuários (Admin)", description = "Retorna todos os usuários do sistema com paginação")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Page<UserResponseDTO>>> getAll(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(APIResponse.success(userService.findAll(pageable)));
    }

    @Operation(summary = "Buscar Usuário por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<UserResponseDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(APIResponse.success(userService.findById(id)));
    }
}