package com.lp3.elearning.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.EnrollmentRequestDTO;
import com.lp3.elearning.dto.EnrollmentResponseDTO;
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
    @GetMapping("/{id}/certificate")
    public ResponseEntity<byte[]> getCertificate(@PathVariable Long id) {
        byte[] pdfBytes = certificateService.generateCertificatePdf(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // Define o nome do arquivo para download
        headers.setContentDispositionFormData("attachment", "certificado_" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}