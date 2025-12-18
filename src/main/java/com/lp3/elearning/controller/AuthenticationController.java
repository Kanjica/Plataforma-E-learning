package com.lp3.elearning.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.lp3.elearning.dto.AuthenticationDTO;
import com.lp3.elearning.dto.LoginResponseDTO;
import com.lp3.elearning.dto.RegisterDTO;
import com.lp3.elearning.entities.Instructor;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.entities.UserRole;
import com.lp3.elearning.repository.InstructorRepository;
import com.lp3.elearning.repository.StudentRepository;
import com.lp3.elearning.service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Autenticação", description = "Login e Registro de usuários")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationController(AuthenticationManager authenticationManager, StudentRepository studentRepository, InstructorRepository instructorRepository, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    @Operation(summary = "Realizar Login", description = "Retorna o token JWT")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(data.login(), data.password())
        );

        User user = (User) auth.getPrincipal();
        String token = tokenService.generateToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(token, user.getRole().name(), user.getId(), user.getName()));
    }

    @Operation(summary = "Registrar novo usuário", description = "Cria conta para Aluno ou Instrutor")
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterDTO data){
        Map<String, String> response = new HashMap<>();

        if(studentRepository.findByEmail(data.login()) != null || instructorRepository.findByEmail(data.login()) != null) {
            response.put("message", "Email já cadastrado!");
            return ResponseEntity.badRequest().body(response);
        }

        String encryptedPassword = passwordEncoder.encode(data.password());
        UserRole userRole;
        try {
            userRole = UserRole.valueOf("ROLE_"+data.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            response.put("message", "Role inválido! Use STUDENT ou INSTRUCTOR." + "Sua entrada: " + data.role());
            return ResponseEntity.badRequest().body(response);
        }

        if(userRole == UserRole.ROLE_STUDENT) {
            studentRepository.save(Student.builder().name(data.name()).email(data.login()).password(encryptedPassword).role(userRole).build());
            response.put("message", "Aluno cadastrado com sucesso!");
        } else if(userRole == UserRole.ROLE_INSTRUCTOR) {
            instructorRepository.save(Instructor.builder().name(data.name()).email(data.login()).password(encryptedPassword).role(userRole).build());
            response.put("message", "Instrutor cadastrado com sucesso!");
        }

        return ResponseEntity.ok(response);
    }
}
    // @PostMapping("/register/aluno")
    // public ResponseEntity<Void> registerAluno(@RequestBody @Valid RegisterDTO data){
    //     if(studentRepository.findByEmail(data.login()) != null) return ResponseEntity.badRequest().build(); 

    //     String encryptedPassword = passwordEncoder.encode(data.password());

    //     this.studentRepository.save(
    //         Student.builder()
    //             .name(data.name())
    //             .email(data.login())
    //             .password(encryptedPassword)
    //             .role(UserRole.ROLE_STUDENT)
    //             .build()
    //     );

    //     return ResponseEntity.ok().build();
    // }

    // @PostMapping("/register/instrutor")
    // public ResponseEntity<Void> registerInstrutor(@RequestBody @Valid RegisterDTO data){
    //     if(instructorRepository.findByEmail(data.login()) != null) return ResponseEntity.badRequest().build();

    //     String encryptedPassword = passwordEncoder.encode(data.password());

    //     this.instructorRepository.save(
    //         Instructor.builder()
    //             .name(data.name())
    //             .email(data.login())
    //             .password(encryptedPassword)
    //             .role(UserRole.ROLE_INSTRUCTOR)
    //             .build()
    //     );

    //     return ResponseEntity.ok().build();
    // }