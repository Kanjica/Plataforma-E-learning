package com.lp3.elearning.mapper;

import org.mapstruct.Mapper;

import com.lp3.elearning.dto.course.CourseResponseDTO;
import com.lp3.elearning.entities.Course;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, InstructorMapper.class, ModuleMapper.class})
public interface CourseMapper {
    CourseResponseDTO toResponseDTO(Course course);
}
