package com.lp3.elearning.mapper;

import com.lp3.elearning.dto.audit.AuditResponseDTO;
import com.lp3.elearning.entities.AuditLog;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditMapper {
    AuditResponseDTO toResponseDTO(AuditLog auditLog);

    default String map(OffsetDateTime value) {
        if (value == null) return null;
        return value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}