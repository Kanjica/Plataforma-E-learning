package com.lp3.elearning.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.EnrollmentRequestDTO;
import com.lp3.elearning.dto.EnrollmentResponseDTO;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.service.CertificateService;
import com.lp3.elearning.service.EnrollmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final CertificateService certificateService;

    public EnrollmentController(EnrollmentService enrollmentService, CertificateService certificateService) {
        this.enrollmentService = enrollmentService;
        this.certificateService = certificateService;
    }

    @PostMapping("/create")
    public ResponseEntity<EnrollmentResponseDTO> create(@RequestBody @Valid EnrollmentRequestDTO request){
        return ResponseEntity.ok(enrollmentService.create(request));
    }

    // ENDPOINT DE DOWNLOAD 
    @GetMapping("/{enrollmentId}/certificate")
    public ResponseEntity<byte[]> getCertificate(
        @PathVariable Long enrollmentId,
        @AuthenticationPrincipal Student student) {

        byte[] pdfBytes = certificateService.generateCertificatePdf(enrollmentId, student);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Define o nome do arquivo para download
        headers.setContentDispositionFormData("attachment", "certificado_" + enrollmentId + ".pdf");

        String filename = "certificado_matricula_" + enrollmentId + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                // Define o cabeçalho para forçar o download (attachment) e sugere o nome do arquivo
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                // Define o tamanho do conteúdo
                .contentLength(pdfBytes.length)
                // Retorna o corpo do arquivo (byte array)
                .body(pdfBytes);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponseDTO>> getByStudent(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
            enrollmentService.findByStudent(studentId)
        );
    }
}