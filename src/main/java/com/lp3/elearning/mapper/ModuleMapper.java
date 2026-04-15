package com.lp3.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lp3.elearning.dto.course.ModuleRequestDTO;
import com.lp3.elearning.dto.course.ModuleResponseDTO;
import com.lp3.elearning.entities.Module;

@Mapper(componentModel = "spring")
public interface ModuleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    Module toEntity(ModuleRequestDTO request);

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseTitle", source = "course.title")
    ModuleResponseDTO toResponseDTO(Module module);
}
