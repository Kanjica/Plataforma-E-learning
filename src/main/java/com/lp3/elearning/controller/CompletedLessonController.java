package com.lp3.elearning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.CompletedLessonResponseDTO;
import com.lp3.elearning.dto.EnrollmentRequestDTO;
import com.lp3.elearning.service.CompletedLessonsService;

@RestController
@RequestMapping("/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/completed")
public class CompletedLessonController {

    private final CompletedLessonsService completedLessonsService;

    public CompletedLessonController(CompletedLessonsService completedLessonsService){
        this.completedLessonsService = completedLessonsService;
    }
    
    @PostMapping
    public ResponseEntity<CompletedLessonResponseDTO> completeLesson(
        @PathVariable Long courseId,
        @PathVariable Long moduleId,
        @PathVariable Long lessonId,
        @RequestBody EnrollmentRequestDTO enrollmentRequest){
        
        return ResponseEntity.ok(completedLessonsService.completeLesson(
            enrollmentRequest,
            lessonId
        ));
    }
}
