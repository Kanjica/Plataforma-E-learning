package com.lp3.elearning.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.CourseRequestDTO;
import com.lp3.elearning.dto.CourseResponseDTO;
import com.lp3.elearning.service.CourseService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/courses")
public class CourseController {

    //localhost:8080/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}
    
    private final CourseService courseService;

    public CourseController(CourseService courseService){
        this.courseService = courseService;
    }

    @PostMapping("/create")
    public ResponseEntity<CourseResponseDTO> create(@RequestBody @Valid CourseRequestDTO courseRequest){
        return ResponseEntity.ok(courseService.createCourse(courseRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long id){
        return ResponseEntity.ok(courseService.getCourseByIdResponseDTO(id));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses(){
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CourseResponseDTO> update(@PathVariable Long id, @RequestBody @Valid CourseRequestDTO courseRequest){
        return ResponseEntity.ok(courseService.updateCourse(id, courseRequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
