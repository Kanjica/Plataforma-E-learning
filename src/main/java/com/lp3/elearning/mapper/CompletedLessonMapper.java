package com.lp3.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.lp3.elearning.dto.enrollment.CompletedLessonResponseDTO;
import com.lp3.elearning.entities.CompletedLesson;
import com.lp3.elearning.service.EnrollmentService;

@Mapper(componentModel = "spring")
public abstract class CompletedLessonMapper {

    @Autowired
    protected EnrollmentService enrollmentService;

    @Mapping(target = "lessonId", source = "lesson.id")
    @Mapping(target = "overallProgress", expression = "java(enrollmentService.calculateOverallProgress(completedLesson.getEnrollment()))")
    public abstract CompletedLessonResponseDTO toResponseDTO(CompletedLesson completedLesson);
}