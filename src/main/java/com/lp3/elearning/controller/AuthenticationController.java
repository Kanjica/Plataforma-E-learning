package com.lp3.elearning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.lp3.elearning.dto.AuthenticationDTO;
import com.lp3.elearning.dto.LoginResponseDTO;
import com.lp3.elearning.dto.RegisterDTO;
import com.lp3.elearning.repository.AlunoRepository;
import com.lp3.elearning.repository.InstrutorRepository;
import com.lp3.elearning.entities.Aluno;
import com.lp3.elearning.entities.Instrutor;
import com.lp3.elearning.service.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final AlunoRepository alunoRepository;
    private final InstrutorRepository instrutorRepository;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    AlunoRepository alunoRepository,
                                    InstrutorRepository instrutorRepository,
                                    TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.alunoRepository = alunoRepository;
        this.instrutorRepository = instrutorRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserDetails) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register/aluno")
    public ResponseEntity<Void> registerAluno(@RequestBody @Valid RegisterDTO data){
        if(alunoRepository.findByEmail(data.login()) != null) return ResponseEntity.badRequest().build(); 

        String encryptedPassword = passwordEncoder.encode(data.password());

        this.alunoRepository.save(
            Aluno.builder()
                .nome(data.name())
                .email(data.login())
                .senha(encryptedPassword)
                .build()
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/instrutor")
    public ResponseEntity<Void> registerInstrutor(@RequestBody @Valid RegisterDTO data){
        if(instrutorRepository.findByEmail(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = passwordEncoder.encode(data.password());

        this.instrutorRepository.save(
            Instrutor.builder()
                .nome(data.name())
                .email(data.login())
                .senha(encryptedPassword)
                .build()
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<String> teste(){
        return ResponseEntity.ok("Deu bom");
    }

}