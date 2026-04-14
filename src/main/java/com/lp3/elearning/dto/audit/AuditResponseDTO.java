package com.lp3.elearning.dto.audit;

public record AuditResponseDTO(
    Long id,
    String action,
    String username,
    String details,
    String timestamp
) {}