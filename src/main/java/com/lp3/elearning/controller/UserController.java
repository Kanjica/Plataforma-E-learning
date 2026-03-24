package com.lp3.elearning.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.lp3.elearning.dto.user.UserResponseDTO;
import com.lp3.elearning.dto.user.UserUpdateRequestDTO;
import com.lp3.elearning.entities.User;
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
    public ResponseEntity<UserResponseDTO> getMyProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.findById(user.getId()));
    }

    @Operation(summary = "Atualizar Meu Perfil")
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMyProfile(
            @AuthenticationPrincipal User user, 
            @RequestBody @Valid UserUpdateRequestDTO request) {
        return ResponseEntity.ok(userService.updateProfile(user.getId(), request));
    }

    @Operation(summary = "Listar Usuários (Admin)")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(summary = "Buscar Usuário por ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
}