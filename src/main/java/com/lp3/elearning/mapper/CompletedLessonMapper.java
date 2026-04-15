package com.lp3.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.lp3.elearning.dto.enrollment.CompletedLessonResponseDTO;
import com.lp3.elearning.entities.CompletedLesson;
import com.lp3.elearning.service.ProgressService;

@Mapper(componentModel = "spring")
public abstract class CompletedLessonMapper {

    @Autowired
    protected ProgressService progressService; 

    @Mapping(target = "lessonId", source = "completedLesson.lesson.id")
    @Mapping(target = "overallProgress", expression = "java(progressService.calculateOverallProgress(completedLesson.getEnrollment().getId(), completedLesson.getEnrollment().getCourse().getId()))")
    public abstract CompletedLessonResponseDTO toResponseDTO(CompletedLesson completedLesson);
}