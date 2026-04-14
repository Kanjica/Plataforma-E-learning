package com.lp3.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lp3.elearning.dto.forum.ReviewResponseDTO;
import com.lp3.elearning.entities.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    
    @Mapping(target = "studentName", source = "student.name")
    ReviewResponseDTO toResponseDTO(Review review);
}
