package com.lp3.elearning.mapper;

import org.mapstruct.Mapper;

import com.lp3.elearning.dto.user.StudentResponseDTO;
import com.lp3.elearning.entities.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentResponseDTO toResponseDTO(Student student);
}
