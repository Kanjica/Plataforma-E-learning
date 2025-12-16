package com.lp3.elearning.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.ResponseRequestDTO;
import com.lp3.elearning.dto.ResponseResponseDTO;
import com.lp3.elearning.service.ResponseService;
import com.lp3.elearning.exception.BusinessRuleException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/topics/{topicId}/responses")
public class ResponseController {

    private final ResponseService responseService;

    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }
    
    // [1] Criar uma nova Resposta (Comentário ou Reply)
    // POST /topics/{topicId}/responses
    @PostMapping
    public ResponseEntity<ResponseResponseDTO> createResponse(
        @PathVariable Long topicId,
        @RequestBody @Valid ResponseRequestDTO request) {

        if (!topicId.equals(request.topicId())) {
            throw new BusinessRuleException("O ID do tópico na URL não corresponde ao ID do corpo da requisição.");
        }
        
        ResponseResponseDTO response = responseService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // [2] Listar as Respostas Raízes (Primeiro Nível, com os threads aninhados) de um Tópico
    // GET /topics/{topicId}/responses
    @GetMapping
    public ResponseEntity<List<ResponseResponseDTO>> getRootResponsesByTopic(@PathVariable Long topicId) {
        // O Service deve carregar apenas as respostas de primeiro nível, e as filhas serão carregadas recursivamente no DTO
        List<ResponseResponseDTO> responses = responseService.findRootResponsesByTopic(topicId);
        return ResponseEntity.ok(responses);
    }
    
    // [3] Deletar uma Resposta
    // DELETE /topics/{topicId}/responses/{responseId}
    @DeleteMapping("/{responseId}")
    public ResponseEntity<Void> deleteResponse(@PathVariable Long topicId, @PathVariable Long responseId) {
        // O Service deve validar a permissão e a existência da resposta
        responseService.delete(responseId);
        return ResponseEntity.noContent().build();
    }
    
    // Nota: O endpoint para UPDATE (PUT) de uma resposta específica também deve ser adicionado.
}