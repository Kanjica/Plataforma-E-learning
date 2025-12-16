package com.lp3.elearning.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.EnrollmentResponseDTO;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.service.EnrollmentService;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final EnrollmentService enrollmentService;

    public StudentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // Endpoint do Dashboard: GET /students/dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<List<EnrollmentResponseDTO>> getMyDashboard(@AuthenticationPrincipal User user) {
        
        // Verifica se o usuário logado é realmente um aluno
        if (!(user instanceof Student)) {
            throw new BusinessRuleException("Apenas alunos podem acessar o dashboard de cursos.");
        }

        // Busca as matrículas usando o ID do usuário logado (token JWT)
        List<EnrollmentResponseDTO> myCourses = enrollmentService.getMyEnrollments(user.getId());
        
        return ResponseEntity.ok(myCourses);
    }
}