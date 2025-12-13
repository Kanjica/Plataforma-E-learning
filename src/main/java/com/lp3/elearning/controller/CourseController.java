package com.lp3.elearning.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.CourseRequestDTO;
import com.lp3.elearning.dto.CourseResponseDTO;
import com.lp3.elearning.service.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/courses")
public class CourseController {
    
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/create")
    public ResponseEntity<CourseResponseDTO> create(@RequestBody @Valid CourseRequestDTO courseRequest) {
        return ResponseEntity.ok(courseService.createCourse(courseRequest));

    }
}
