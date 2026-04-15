package com.lp3.elearning.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lp3.elearning.dto.user.UserResponseDTO;
import com.lp3.elearning.dto.user.UserUpdateRequestDTO;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.entities.UserRole;
import com.lp3.elearning.mapper.UserMapper;
import com.lp3.elearning.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks private UserService userService;
    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;

    @Test
    void shouldUpdateProfile() {
        long userId = 1L;
        User user = new User(); 
        user.setId(userId); 
        user.setName("Old");
        user.setRole(UserRole.ROLE_STUDENT);

        UserUpdateRequestDTO dto = new UserUpdateRequestDTO("New Name", "new@email.com");

        var responseDTO = new UserResponseDTO(
            userId,
            "New Name",
            "new@email.com",
            UserRole.ROLE_STUDENT
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        when(userMapper.toResponseDTO(any(User.class))).thenReturn(responseDTO);

        var result = userService.updateProfile(1L, dto);    

        assertEquals("New Name", result.name());
        assertEquals("new@email.com", result.email());
    }
}