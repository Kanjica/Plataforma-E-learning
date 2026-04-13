package com.lp3.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lp3.elearning.dto.course.CategoryRequestDTO;
import com.lp3.elearning.dto.course.CategoryResponseDTO;
import com.lp3.elearning.entities.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryRequestDTO categoryDTO);

    CategoryResponseDTO toResponseDTO(Category category);
}
