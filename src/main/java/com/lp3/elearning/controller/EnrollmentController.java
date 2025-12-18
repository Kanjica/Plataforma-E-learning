package com.lp3.elearning.controller;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.lp3.elearning.dto.EnrollmentRequestDTO;
import com.lp3.elearning.dto.EnrollmentResponseDTO;
import com.lp3.elearning.entities.Student;
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
    @PostMapping // ANTES: /create (Removido)
    public ResponseEntity<EnrollmentResponseDTO> create(@RequestBody @Valid EnrollmentRequestDTO request){
        return ResponseEntity.ok(enrollmentService.create(request));
    }

    @Operation(summary = "Baixar Certificado", description = "Gera o PDF se o curso estiver concluído")
    @GetMapping("/{enrollmentId}/certificate")
    public ResponseEntity<byte[]> getCertificate(
        @PathVariable Long enrollmentId,
        @AuthenticationPrincipal Student student) {

        byte[] pdfBytes = certificateService.generateCertificatePdf(enrollmentId, student);
        String filename = "certificado_matricula_" + enrollmentId + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }

    @Operation(summary = "Listar por Aluno (Admin)", description = "Lista matrículas de um aluno específico")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponseDTO>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.findByStudent(studentId));
    }
}