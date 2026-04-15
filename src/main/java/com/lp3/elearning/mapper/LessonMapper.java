package com.lp3.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lp3.elearning.dto.course.LessonRequestDTO;
import com.lp3.elearning.dto.course.LessonResponseDTO;
import com.lp3.elearning.entities.Lesson;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "module", ignore = true)
    Lesson toEntity(LessonRequestDTO lessonRequestDTO);
    
    @Mapping(source = "module.id", target = "moduleId")
    @Mapping(source = "module.title", target = "moduleTitle")
    LessonResponseDTO toResponseDTO(Lesson lesson);
}
