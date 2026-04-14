package com.lp3.elearning.mapper;

import org.mapstruct.Mapper;

import com.lp3.elearning.dto.user.InstructorResponseDTO;
import com.lp3.elearning.entities.Instructor;

@Mapper(componentModel = "spring")
public interface InstructorMapper {
    
    InstructorResponseDTO toResponseDTO(Instructor instructor);
}
