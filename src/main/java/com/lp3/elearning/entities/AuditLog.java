package com.lp3.elearning.entities;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter @Setter 
@Builder @AllArgsConstructor @NoArgsConstructor
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action; // Ex: "DELETE_COURSE"

    @Column(nullable = false)
    private String username; // Quem fez

    @Column(columnDefinition = "TEXT")
    private String details; // Nome do método ou parâmetros

    private String ipAddress;

    @Column(nullable = false)
    private OffsetDateTime timestamp;
}