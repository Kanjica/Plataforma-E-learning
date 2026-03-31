package com.lp3.elearning.controller;

import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.dto.course.CourseResponseDTO;
import com.lp3.elearning.dto.user.InstructorResponseDTO;
import com.lp3.elearning.service.InstructorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/instructors")
@Tag(name = "Instrutores", description = "Visualização pública de perfis de instrutores")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @Operation(summary = "Perfil do Instrutor", description = "Busca detalhes públicos de um instrutor")
    @GetMapping("/{instructorId}")
    public ResponseEntity<APIResponse<InstructorResponseDTO>> getInstructorById(@PathVariable Long instructorId) {
        return ResponseEntity.ok(APIResponse.success(instructorService.findById(instructorId)));
    }

    @Operation(summary = "Cursos do Instrutor", description = "Lista todos os cursos ministrados por este instrutor")
    @GetMapping("/{instructorId}/my-courses")
    public ResponseEntity<APIResponse<Set<CourseResponseDTO>>> getInstructorCourses(@PathVariable Long instructorId) {
        return ResponseEntity.ok(APIResponse.success(instructorService.findCoursesByInstructorId(instructorId)));
    }
}