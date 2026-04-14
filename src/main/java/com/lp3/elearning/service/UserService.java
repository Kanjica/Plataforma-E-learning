package com.lp3.elearning.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.user.UserResponseDTO;
import com.lp3.elearning.dto.user.UserUpdateRequestDTO;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.mapper.UserMapper;
import com.lp3.elearning.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado"));
        return userMapper.toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateProfile(Long userId, UserUpdateRequestDTO request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado"));
        
        user.setName(request.name());
        user.setEmail(request.email()); 
        
        return userMapper.toResponseDTO(userRepository.save(user));
    }

    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponseDTO);
    }
}
