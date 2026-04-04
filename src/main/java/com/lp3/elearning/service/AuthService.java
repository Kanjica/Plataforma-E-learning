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
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, TokenService tokenService,
                       UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(AuthenticationDTO data) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(data.login(), data.password())
        );

        User user = (User) auth.getPrincipal();
        return new LoginResponseDTO(tokenService.generateToken(user), user.getRole().name(), user.getId(), user.getName());
    }

    @Transactional
    public void validateAndPrepare(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email já cadastrado!");
        }
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}