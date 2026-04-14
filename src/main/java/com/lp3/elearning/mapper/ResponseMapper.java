package com.lp3.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lp3.elearning.dto.forum.ResponseResponseDTO;
import com.lp3.elearning.entities.Response;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public abstract class ResponseMapper {

    
    @Mapping(target = "topicId", source = "topic.id")
    @Mapping(target = "responseParentId", source = "responseParent.id")
    @Mapping(target = "hasChildren", expression = "java(response.getResponses() != null && !response.getResponses().isEmpty())")
    @Mapping(target = "childrenCount", expression = "java(response.getResponses() != null ? response.getResponses().size() : 0)")
    public abstract ResponseResponseDTO toResponseDTO(Response response);
}