package com.lp3.elearning.controller;

import java.net.URI;
import java.util.Set;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.dto.enrollment.CompletedLessonResponseDTO;
import com.lp3.elearning.dto.enrollment.EnrollmentRequestDTO;
import com.lp3.elearning.dto.enrollment.EnrollmentResponseDTO;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.security.anottation.CurrentUser;
import com.lp3.elearning.service.CertificateService;
import com.lp3.elearning.service.EnrollmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/enrollments")
@Tag(name = "Matrículas", description = "Gestão de inscrição em cursos e certificação")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final CertificateService certificateService;

    public EnrollmentController(EnrollmentService enrollmentService, CertificateService certificateService) {
        this.enrollmentService = enrollmentService;
        this.certificateService = certificateService;
    }

    @Operation(summary = "Realizar Matrícula", description = "Inscreve o aluno em um curso")
    @PostMapping 
    @PreAuthorize("hasRole('ADMIN') or @enrollmentSecurity.isOwner(#request.enrollmentId, #user.id)")
    public ResponseEntity<APIResponse<EnrollmentResponseDTO>> create(
            @RequestBody @Valid EnrollmentRequestDTO request,
            @CurrentUser User user
    ){
        var createdEnrollment = enrollmentService.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdEnrollment.id())
            .toUri();
            
        return ResponseEntity.created(location).body(APIResponse.success(createdEnrollment));
    }

    @Operation(summary = "Baixar Certificado", description = "Gera o PDF se o curso estiver concluído")
    @GetMapping("/{enrollmentId}/certificate")
    @PreAuthorize("hasRole('ADMIN') or @enrollmentSecurity.isOwner(#request.enrollmentId, #user.id)")
    public ResponseEntity<APIResponse<byte[]>> getCertificate(
            @PathVariable Long enrollmentId,
            @CurrentUser User user
    ){
        Student student = (Student) user; 
        byte[] pdfBytes = certificateService.generateCertificatePdf(enrollmentId, student);
        String filename = "certificado_matricula_" + enrollmentId + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentLength(pdfBytes.length)
                .body(APIResponse.success(pdfBytes));
    }

    @Operation(summary = "Listar por Aluno (Admin)", description = "Lista matrículas de um aluno específico")
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Page<EnrollmentResponseDTO>>> getByStudent(
            @ParameterObject Pageable pageable,
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(APIResponse.success(enrollmentService.findByStudent(studentId, pageable)));
    }

    @Operation(summary = "Meu Dashboard", description = "Cursos em que o aluno logado está matriculado (Paginado)")
    @GetMapping("/me") 
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<APIResponse<Page<EnrollmentResponseDTO>>> getMyEnrollments(
            @CurrentUser User user, 
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(APIResponse.success(enrollmentService.findByStudent(user.getId(), pageable)));
    }

    @Operation(summary = "Ver Progresso", description = "Retorna as aulas concluídas de uma matrícula")
    @GetMapping("/{enrollmentId}/progress")
    @PreAuthorize("hasRole('ADMIN') or @enrollmentSecurity.isOwner(#request.enrollmentId, #user.id)")
    public ResponseEntity<APIResponse<Set<CompletedLessonResponseDTO>>> getProgress(
        @PathVariable Long enrollmentId,
        @CurrentUser User user){
        
        var enrollment = enrollmentService.findById(enrollmentId);
        return ResponseEntity.ok(APIResponse.success(enrollmentService.calculateProgress(enrollment)));
    }
}