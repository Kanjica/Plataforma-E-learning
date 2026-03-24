package com.lp3.elearning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.enrollment.CompletedLessonResponseDTO;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.repository.StudentRepository;
import com.lp3.elearning.service.CompletedLessonsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/courses/{courseId}/modules/{moduleId}/lessons/{lessonId}/completed")
@Tag(name = "Progresso de Aulas", description = "Marcação de aulas como concluídas")
public class CompletedLessonController {

    private final CompletedLessonsService service;
    private final StudentRepository studentRepository;

    public CompletedLessonController(CompletedLessonsService service, StudentRepository studentRepository){ this.service = service; this.studentRepository = studentRepository;}
    
    @Operation(summary = "Concluir Aula", description = "Marca a aula como assistida e atualiza o progresso")
    @PostMapping
    public ResponseEntity<CompletedLessonResponseDTO> completeLesson(
        @PathVariable Long courseId, @PathVariable Long lessonId, @AuthenticationPrincipal UserDetails userDetails){
            String email = userDetails.getUsername();
            Student student = (Student) studentRepository.findByEmail(email);
        return ResponseEntity.ok(service.completeLesson(student.getId(), courseId, lessonId));
    }
}