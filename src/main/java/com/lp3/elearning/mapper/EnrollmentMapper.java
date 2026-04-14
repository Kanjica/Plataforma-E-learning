package com.lp3.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lp3.elearning.dto.enrollment.EnrollmentResponseDTO;
import com.lp3.elearning.entities.Enrollment;

@Mapper(componentModel = "spring", uses = { CompletedLessonMapper.class})
public interface EnrollmentMapper {
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseTitle", source = "course.title")
    EnrollmentResponseDTO toResponseDTO(Enrollment enrollment);
}
