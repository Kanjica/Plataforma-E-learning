package com.lp3.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.lp3.elearning.dto.course.CourseListDTO;
import com.lp3.elearning.dto.course.CourseRequestDTO;
import com.lp3.elearning.dto.course.CourseResponseDTO;
import com.lp3.elearning.entities.Course;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, InstructorMapper.class, ModuleMapper.class})
public interface CourseMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instructors", ignore = true)
    @Mapping(target = "modules", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "totalReviews", ignore = true)
    Course toEntity(CourseRequestDTO courseRequestDTO);

    CourseResponseDTO toResponseDTO(Course course);

    @Mapping(target = "categoryNames", expression = "java(course.getCategories().stream().map(c -> c.getName()).toList())")
    @Mapping(target = "instructorNames", expression = "java(course.getInstructors().stream().map(i -> i.getName()).toList())")
    CourseListDTO toListDTO(Course course);

    @Mapping(target = "title" , source = "courseRequestDTO.title")
    @Mapping(target = "description" , source = "courseRequestDTO.description")
    @Mapping(target = "imageUrl" , source = "courseRequestDTO.imageUrl")
    @Mapping(target = "workload" , source = "courseRequestDTO.workload")
    @Mapping(target = "price" , source = "courseRequestDTO.price")
    @Mapping(target = "oldPrice" , source = "courseRequestDTO.oldPrice")
    @Mapping(target = "isBestSeller" , source = "courseRequestDTO.isBestSeller")
    Course updateCourseFromRequestDTO(CourseRequestDTO courseRequestDTO, Course course);
}
