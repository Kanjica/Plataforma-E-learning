package com.lp3.elearning.dto.course;

import java.util.List;

public record CourseListDTO(
    Long id,
    String title,
    List<String> categoryNames, 
    List<String> instructorNames 
) {
}