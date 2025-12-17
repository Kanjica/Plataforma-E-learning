package com.lp3.elearning.service;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.UserResponseDTO;
import com.lp3.elearning.entities.User;

@Service
public class UserService {
    
    public UserService(){}

    public UserResponseDTO toResponseDTO(User user){
        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
        );
    }

}
