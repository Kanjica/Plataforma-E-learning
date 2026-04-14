package com.lp3.elearning.mapper;

import org.mapstruct.Mapper;

import com.lp3.elearning.dto.user.UserResponseDTO;
import com.lp3.elearning.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toResponseDTO(User user);
}
