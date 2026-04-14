package com.lp3.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lp3.elearning.dto.enrollment.EnrollmentRequestDTO;
import com.lp3.elearning.dto.enrollment.EnrollmentResponseDTO;
import com.lp3.elearning.entities.Enrollment;

@Mapper(componentModel = "spring", uses = { CompletedLessonMapper.class})
public interface EnrollmentMapper {

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "enrollmentDate", ignore = true)
    @Mapping(target = "completedLessons", ignore = true) 
    @Mapping(target = "overallProgress", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "id", ignore = true)
    Enrollment toEntity(EnrollmentRequestDTO responseDTO);

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseTitle", source = "course.title")
    EnrollmentResponseDTO toResponseDTO(Enrollment enrollment);
}
