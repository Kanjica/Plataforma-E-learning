package com.lp3.elearning.service;

import com.lp3.elearning.dto.auth.*;
import com.lp3.elearning.entities.*;
import com.lp3.elearning.exception.ConflictException;
import com.lp3.elearning.repository.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, TokenService tokenService,
                       StudentRepository studentRepository, InstructorRepository instructorRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(AuthenticationDTO data) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(data.login(), data.password())
        );

        User user = (User) auth.getPrincipal();
        String token = tokenService.generateToken(user);

        return new LoginResponseDTO(token, user.getRole().name(), user.getId(), user.getName());
    }

    @Transactional
    public void register(RegisterDTO data) {
        if (studentRepository.findByEmail(data.login()) != null || instructorRepository.findByEmail(data.login()) != null) {
            throw new ConflictException("Email já cadastrado!");
        }

        UserRole userRole = parseRole(data.role());
        String encryptedPassword = passwordEncoder.encode(data.password());

        if (userRole == UserRole.ROLE_STUDENT) {
            studentRepository.save(Student.builder()
                    .name(data.name()).email(data.login())
                    .password(encryptedPassword).role(userRole).build());
        }
        else{
            instructorRepository.save(Instructor.builder()
                    .name(data.name()).email(data.login())
                    .password(encryptedPassword).role(userRole).build());
        }
    }

    private UserRole parseRole(String roleStr){
        try{
            return UserRole.valueOf("ROLE_" + roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Role inválido! Use STUDENT ou INSTRUCTOR.");
        }
    }
}