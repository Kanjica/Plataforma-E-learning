package com.lp3.elearning.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.lp3.elearning.dto.*;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.service.CourseService;
import com.lp3.elearning.service.EnrollmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/courses")
@Tag(name = "Cursos", description = "Gestão do catálogo de cursos e progresso")
public class CourseController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    public CourseController(CourseService courseService, EnrollmentService enrollmentService){
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @Operation(summary = "Criar Curso", description = "Cadastra um novo curso na plataforma")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Curso criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping 
    public ResponseEntity<CourseResponseDTO> create(@RequestBody @Valid CourseRequestDTO courseRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(courseRequest));
    }

    @Operation(summary = "Buscar Curso por ID")
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long courseId){
        return ResponseEntity.ok(courseService.getCourseByIdResponseDTO(courseId));
    }

    @Operation(summary = "Listar todos os cursos")
    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses(){
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @Operation(summary = "Atualizar Curso")
    @PutMapping("/{courseId}") 
    public ResponseEntity<CourseResponseDTO> update(@PathVariable Long courseId, @RequestBody @Valid CourseRequestDTO courseRequest){
        return ResponseEntity.ok(courseService.updateCourse(courseId, courseRequest));
    }

    @Operation(summary = "Deletar Curso")
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> delete(@PathVariable Long courseId){
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Filtrar Cursos", description = "Busca avançada de cursos")
    @PostMapping("/search") 
    public ResponseEntity<Set<CourseResponseDTO>> filterCourses(@RequestBody @Valid CourseFilterDTO request){
        return ResponseEntity.ok(courseService.filterCourses(request));
    }

    @Operation(summary = "Ver Progresso", description = "Retorna o progresso do aluno logado neste curso")
    @GetMapping("/{courseId}/progress")
    public ResponseEntity<Set<CompletedLessonResponseDTO>> progress(
        @AuthenticationPrincipal Student student, 
        @PathVariable Long courseId) {
        
        var enrollment = enrollmentService.findByStudentIdAndCourseId(student.getId(), courseId);
        return ResponseEntity.ok(enrollmentService.calculateProgress(enrollment));
    }
}