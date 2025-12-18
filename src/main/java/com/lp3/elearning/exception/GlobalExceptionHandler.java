package com.lp3.elearning.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // [1] Erro Genérico (Fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        // Em produção, é bom logar o erro aqui (ex: log.error(...))
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", ex.getMessage());
    }

    // [2] Regras de Negócio e Conflitos
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> handleConflictException(ConflictException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Conflito de dados", ex.getMessage());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Object> handleBusinessRuleException(BusinessRuleException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Regra de negócio violada", ex.getMessage());
    }

    // [3] Recursos não encontrados (404)
    @ExceptionHandler({ResourceNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage());
    }

    // [4] Segurança (401 e 403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Acesso negado", "Você não tem permissão para acessar este recurso.");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Falha na autenticação", "Usuário ou senha inválidos.");
    }

    // [5] Validação de Campos (@Valid falhou) - O MAIS IMPORTANTE PARA O FRONTEND
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Erro de validação");
        
        // Extrai apenas os campos e mensagens amigáveis
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        body.put("message", "Verifique os campos inválidos");
        body.put("fieldErrors", fieldErrors); // Retorna { "email": "Inválido", "idade": "Deve ser maior que 18" }

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // [6] Erros de Banco de Dados (Constraint)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex){
        return buildResponse(HttpStatus.CONFLICT, "Violação de integridade", "Erro ao salvar dados. Verifique duplicidades.");
    }

    // [7] Erros de Token JWT
    @ExceptionHandler({JWTCreationException.class, JWTVerificationException.class})
    public ResponseEntity<Object> handleJWTException(RuntimeException ex){
        return buildResponse(HttpStatus.UNAUTHORIZED, "Erro de Token", "Falha na geração ou verificação do token de acesso.");
    }

    // Método auxiliar para evitar repetição de código
    private ResponseEntity<Object> buildResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}