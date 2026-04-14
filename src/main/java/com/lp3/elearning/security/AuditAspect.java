package com.lp3.elearning.security;

import com.lp3.elearning.entities.AuditLog;
import com.lp3.elearning.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;
import java.util.Arrays;

import com.lp3.elearning.security.anottation.Auditable;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditLogRepository auditRepository;
    private final HttpServletRequest request;

    @Around("@annotation(auditable)")
    public Object auditAction(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // Executa o método original
        Object result = joinPoint.proceed();
        
        String args = Arrays.toString(joinPoint.getArgs());
        String details = "Method: " + joinPoint.getSignature().getName() + " | Args: " + args;  

        // Se o método terminou com sucesso, salva a auditoria
        AuditLog auditLog = AuditLog.builder()
            .action(auditable.action())
            .username(username)
            .ipAddress(request.getRemoteAddr())
            .details(details)
            .timestamp(OffsetDateTime.now())
            .build();

        auditRepository.save(auditLog);
        return result;
    }
}