package com.lp3.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lp3.elearning.dto.course.ModuleResponseDTO;
import com.lp3.elearning.entities.Module;

@Mapper(componentModel = "spring")
public interface ModuleMapper {
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseTitle", source = "course.title")
    ModuleResponseDTO toResponseDTO(Module module);
}
