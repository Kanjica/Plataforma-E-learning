package com.lp3.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.lp3.elearning.dto.forum.TopicRequestDTO;
import com.lp3.elearning.dto.forum.TopicResponseDTO;
import com.lp3.elearning.entities.Topic;
import com.lp3.elearning.service.CourseService;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CourseService.class})
public abstract class TopicMapper {

    @Autowired
    protected CourseService courseService;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "responses", ignore = true)
    @Mapping(target = "user", ignore = true) // O User será setado manualmente ou via parâmetro
    @Mapping(target = "course", expression = "java(courseService.findById(request.courseId()))")
    public abstract Topic toEntity(TopicRequestDTO request);

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseTitle", source = "course.title")
    @Mapping(target = "responseCount", expression = "java(topic.getResponses() != null ? topic.getResponses().size() : 0)")
    public abstract TopicResponseDTO toResponseDTO(Topic topic);
}