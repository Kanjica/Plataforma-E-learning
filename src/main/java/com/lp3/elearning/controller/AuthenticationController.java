package com.lp3.elearning.controller;
import com.lp3.elearning.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.lp3.elearning.dto.auth.AuthenticationDTO;
import com.lp3.elearning.dto.auth.LoginResponseDTO;
import com.lp3.elearning.dto.auth.RegisterDTO;
import com.lp3.elearning.dto.common.APIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Autenticação", description = "Login e Registro de usuários")
public class AuthenticationController {

    private final AuthService authService;

    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Realizar Login")
    @PostMapping("/login")
    public ResponseEntity<APIResponse<LoginResponseDTO>> login(@RequestBody @Valid AuthenticationDTO data){
        LoginResponseDTO response = authService.login(data);
        return ResponseEntity.ok(APIResponse.success(response));
    }

    @Operation(summary = "Registrar novo usuário")
    @PostMapping("/register")
    public ResponseEntity<APIResponse<String>> register(@RequestBody @Valid RegisterDTO data){
        authService.register(data);
        return ResponseEntity.ok(APIResponse.success("Usuário cadastrado com sucesso!"));
    }
}