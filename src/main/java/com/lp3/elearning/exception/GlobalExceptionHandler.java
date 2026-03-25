package com.lp3.elearning.exception;

import java.util.HashMap;
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
import com.lp3.elearning.dto.common.APIResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // [1] Erro Genérico (Fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>> handleGenericException(Exception ex) {
        
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", ex.getMessage());
    }

    // [2] Regras de Negócio e Conflitos
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<APIResponse<Void>> handleConflictException(ConflictException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Conflito de dados", ex.getMessage());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<APIResponse<Void>> handleBusinessRuleException(BusinessRuleException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Regra de negócio violada", ex.getMessage());
    }

    // [3] Recursos não encontrados (404)
    @ExceptionHandler({ResourceNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<APIResponse<Void>> handleNotFoundException(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage());
    }

    // [4] Segurança (401 e 403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Acesso negado", "Você não tem permissão para acessar este recurso.");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<APIResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Falha na autenticação", "Usuário ou senha inválidos.");
    }

    // [5] Validação de Campos (@Valid falhou) - O MAIS IMPORTANTE PARA O FRONTEND
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "Erro de validação nos campos", fieldErrors);
    }

    // [6] Erros de Banco de Dados (Constraint)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIResponse<Void>> handleConstraintViolationException(ConstraintViolationException ex){
        return buildResponse(HttpStatus.CONFLICT, "Violação de integridade", "Erro ao salvar dados. Verifique duplicidades.");
    }

    // [7] Erros de Token JWT
    @ExceptionHandler({JWTCreationException.class, JWTVerificationException.class})
    public ResponseEntity<APIResponse<Void>> handleJWTException(RuntimeException ex){
        return buildResponse(HttpStatus.UNAUTHORIZED, "Erro de Token", "Falha na geração ou verificação do token de acesso.");
    }

    private ResponseEntity<APIResponse<Void>> buildResponse(HttpStatus status, String mensagem, Object erros) {
        APIResponse<Void> response = APIResponse.error(mensagem, erros);
        return new ResponseEntity<>(response, status);
    }
}