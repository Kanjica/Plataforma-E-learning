package com.lp3.elearning.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lp3.elearning.dto.UserRequestDTO;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks private UserService userService;
    @Mock private UserRepository userRepository;

    @Test
    void shouldUpdateProfile() {
        User user = new User(); user.setId(1L); user.setName("Old");
        UserRequestDTO dto = new UserRequestDTO("New Name", "new@email.com", null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        var result = userService.updateProfile(1L, dto);

        assertEquals("New Name", result.name());
        assertEquals("new@email.com", result.email());
    }
}