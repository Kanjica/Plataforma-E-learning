package com.lp3.elearning.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.user.UserResponseDTO;
import com.lp3.elearning.dto.user.UserUpdateRequestDTO;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado"));
        return toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateProfile(Long userId, UserUpdateRequestDTO request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado"));
        
        user.setName(request.name());
        user.setEmail(request.email()); 
        
        return toResponseDTO(userRepository.save(user));
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
            .map(this::toResponseDTO)
            .toList();
    }

    public UserResponseDTO toResponseDTO(User user){
        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
        );
    }
}
