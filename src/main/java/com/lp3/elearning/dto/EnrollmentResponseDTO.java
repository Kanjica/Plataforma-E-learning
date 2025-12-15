package com.lp3.elearning.dto;

public record EnrollmentResponseDTO(
    Long id,
    StudentResponseDTO student,
    CourseResponseDTO course
) { }
