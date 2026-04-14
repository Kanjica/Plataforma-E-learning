package com.lp3.elearning.controller;

import com.lp3.elearning.dto.audit.AuditResponseDTO;
import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.entities.AuditLog;
import com.lp3.elearning.mapper.AuditMapper;
import com.lp3.elearning.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/audit")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AuditController {

    private final AuditLogRepository auditRepository;
    private final AuditMapper auditMapper;

    @GetMapping
    public ResponseEntity<APIResponse<Page<AuditResponseDTO>>> getAllLogs(@ParameterObject Pageable pageable) {
        Page<AuditLog> logs = auditRepository.findAll(pageable);
        Page<AuditResponseDTO> responseLogs = logs.map(auditMapper::toResponseDTO);

        return ResponseEntity.ok(APIResponse.success(responseLogs, "Logs de auditoria listados com sucesso!"));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<APIResponse<Page<AuditResponseDTO>>> getLogsByUser(
            @PathVariable String username, Pageable pageable
    ) {
        Page<AuditLog> logs = auditRepository.findByUsername(username, pageable);
        Page<AuditResponseDTO> responseLogs = logs.map(auditMapper::toResponseDTO);
        return ResponseEntity.ok(APIResponse.success(responseLogs, "Logs do usuário " + username + " listados com sucesso!"));
    }
}