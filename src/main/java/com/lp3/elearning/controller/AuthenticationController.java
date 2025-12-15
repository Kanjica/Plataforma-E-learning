package com.lp3.elearning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.AuthenticationDTO;
import com.lp3.elearning.dto.LoginResponseDTO;
import com.lp3.elearning.dto.RegisterDTO;
import com.lp3.elearning.entities.Instructor;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.entities.UserRole;
import com.lp3.elearning.repository.InstructorRepository;
import com.lp3.elearning.repository.StudentRepository;
import com.lp3.elearning.service.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
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
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserDetails) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register/aluno")
    public ResponseEntity<Void> registerAluno(@RequestBody @Valid RegisterDTO data){
        if(studentRepository.findByEmail(data.login()) != null) return ResponseEntity.badRequest().build(); 

        String encryptedPassword = passwordEncoder.encode(data.password());

        this.studentRepository.save(
            Student.builder()
                .name(data.name())
                .email(data.login())
                .password(encryptedPassword)
                .role(UserRole.STUDENT.getRole())
                .build()
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/instrutor")
    public ResponseEntity<Void> registerInstrutor(@RequestBody @Valid RegisterDTO data){
        if(instructorRepository.findByEmail(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = passwordEncoder.encode(data.password());

        this.instructorRepository.save(
            Instructor.builder()
                .name(data.name())
                .email(data.login())
                .password(encryptedPassword)
                .role(UserRole.INSTRUCTOR.getRole())
                .build()
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<String> teste(){
        return ResponseEntity.ok("Deu bom");
    }

}