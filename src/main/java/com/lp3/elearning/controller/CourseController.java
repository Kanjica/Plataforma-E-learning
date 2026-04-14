package com.lp3.elearning.controller;

import java.net.URI;
import java.util.Set;

import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.dto.course.CourseFilterDTO;
import com.lp3.elearning.dto.course.CourseListDTO;
import com.lp3.elearning.dto.course.CourseRequestDTO;
import com.lp3.elearning.dto.course.CourseResponseDTO;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.security.anottation.CurrentUser;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lp3.elearning.service.CourseService;

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

    public CourseController(CourseService courseService){
        this.courseService = courseService;
    }

    @Operation(summary = "Criar Curso", description = "Cadastra um novo curso na plataforma")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Curso criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping 
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<CourseResponseDTO>> create(
        @RequestBody @Valid CourseRequestDTO courseRequest,
        @CurrentUser User user
    ) {
        var createdCourse = courseService.createCourse(courseRequest, user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdCourse.id())
            .toUri();
            
        return ResponseEntity.created(location).body(APIResponse.success(createdCourse));
    }

    @Operation(summary = "Buscar Curso por ID")
    @GetMapping("/{courseId}")
    public ResponseEntity<APIResponse<CourseResponseDTO>> getCourseById(@PathVariable Long courseId){
        return ResponseEntity.ok(APIResponse.success(courseService.getCourseByIdResponseDTO(courseId)));
    }

    @Operation(summary = "Listar todos os cursos com paginação")
    @GetMapping
    public ResponseEntity<APIResponse<Page<CourseResponseDTO>>> getAllCourses(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(APIResponse.success(courseService.getAllCourses(pageable)));
    }

    @Operation(summary = "Listar cursos com menos dados com paginação e ordenação")
    @GetMapping("/paged")
    public ResponseEntity<APIResponse<Page<CourseListDTO>>> list(
            @PageableDefault(size = 10, sort = "title") Pageable pageable
    ) {
        
        return ResponseEntity.ok(APIResponse.success(courseService.findAllPaged(pageable)));
    }

    @Operation(summary = "Atualizar Curso")
    @PutMapping("/{courseId}") 
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.isInstructorOfCourse(#courseId, #user.id)")
    public ResponseEntity<APIResponse<CourseResponseDTO>> update(
            @PathVariable Long courseId, 
            @RequestBody @Valid CourseRequestDTO courseRequest, 
            @CurrentUser User user
    ){
        return ResponseEntity.ok(APIResponse.success(courseService.updateCourse(courseId, courseRequest)));
    }

    @Operation(summary = "Deletar Curso")
    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.isInstructorOfCourse(#courseId, #user.id)")
    public ResponseEntity<APIResponse<Void>> delete(
            @PathVariable Long courseId,
            @CurrentUser User user
    ){
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok(APIResponse.success(null));
    }

    @Operation(summary = "Filtrar Cursos", description = "Busca avançada de cursos")
    @PostMapping("/search") 
    public ResponseEntity<APIResponse<Set<CourseResponseDTO>>> filterCourses(@RequestBody @Valid CourseFilterDTO request){
        return ResponseEntity.ok(APIResponse.success(courseService.filterCourses(request)));
    }

    @Operation(summary = "Cursos do Instrutor", description = "Lista todos os cursos ministrados por este instrutor")
    @GetMapping("/instructors/{instructorId}/my-courses")
    public ResponseEntity<APIResponse<Set<CourseResponseDTO>>> getInstructorCourses(@PathVariable Long instructorId) {
        return ResponseEntity.ok(APIResponse.success(courseService.findCoursesByInstructorId(instructorId)));
    }
}