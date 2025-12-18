package com.lp3.elearning.controller;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.CompletedLessonResponseDTO;
import com.lp3.elearning.dto.CourseFilterDTO;
import com.lp3.elearning.dto.CourseRequestDTO;
import com.lp3.elearning.dto.CourseResponseDTO;
import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.service.CourseService;
import com.lp3.elearning.service.EnrollmentService;

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
    private final EnrollmentService enrollmentService;

    public CourseController(CourseService courseService, EnrollmentService enrollmentService){
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<CourseResponseDTO> create(@RequestBody @Valid CourseRequestDTO courseRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(courseRequest));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long courseId){
        return ResponseEntity.ok(courseService.getCourseByIdResponseDTO(courseId));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses(){
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PutMapping("/update/{courseId}")
    public ResponseEntity<CourseResponseDTO> update(@PathVariable Long courseId, @RequestBody @Valid CourseRequestDTO courseRequest){
        return ResponseEntity.ok(courseService.updateCourse(courseId, courseRequest));
    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<Void> delete(@PathVariable Long courseId){
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter-courses")
    public ResponseEntity<Set<CourseResponseDTO>> filterCourses(@RequestBody @Valid CourseFilterDTO request){
        return ResponseEntity.ok(courseService.filterCourses(request));
    }

    @GetMapping("/{courseId}/progress")
    public ResponseEntity<Set<CompletedLessonResponseDTO>> progress(
        @AuthenticationPrincipal Student student, 
        @PathVariable Long courseId) {
        
        Enrollment e = enrollmentService.findByStudentIdAndCourseId(student.getId(), courseId);
        return ResponseEntity.ok(enrollmentService.calculateProgress(e));
    }
}
