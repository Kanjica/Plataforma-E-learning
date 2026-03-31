package com.lp3.elearning.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.dto.forum.ResponseRequestDTO;
import com.lp3.elearning.dto.forum.ResponseResponseDTO;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.service.ResponseService;
import com.lp3.elearning.exception.BusinessRuleException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/topics/{topicId}/responses")
@Tag(name = "Fórum - Respostas", description = "Gestão de comentários e replies nos tópicos")
public class ResponseController {

    private final ResponseService responseService;

    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }
    
    @Operation(summary = "Criar Resposta", description = "Adiciona um comentário ou resposta a um tópico")
    @PostMapping
    public ResponseEntity<APIResponse<ResponseResponseDTO>> createResponse(
        @PathVariable Long topicId,
        @RequestBody @Valid ResponseRequestDTO request,
        @AuthenticationPrincipal User user){

        if(!topicId.equals(request.topicId())){
            throw new BusinessRuleException("O ID do tópico na URL não corresponde ao ID do corpo da requisição.");
        }
        
        ResponseResponseDTO response = responseService.create(request, user);
        return ResponseEntity.ok(APIResponse.success(response));
    }

    @Operation(summary = "Listar Respostas", description = "Retorna a árvore de comentários de um tópico")
    @GetMapping
    public ResponseEntity<APIResponse<List<ResponseResponseDTO>>> getRootResponsesByTopic(@PathVariable Long topicId) {
        return ResponseEntity.ok(APIResponse.success(responseService.findRootResponsesByTopic(topicId)));
    }
    
    @Operation(summary = "Deletar Resposta", description = "Remove um comentário (apenas autor ou admin)")
    @DeleteMapping("/{responseId}")
    public ResponseEntity<APIResponse<Void>> deleteResponse(
        @PathVariable Long topicId, 
        @PathVariable Long responseId,
        @AuthenticationPrincipal User user) {
        responseService.delete(responseId, user);
        return ResponseEntity.ok(APIResponse.success(null));
    }
    
    @Operation(summary = "Atualizar Resposta", description = "Edita o conteúdo de um comentário")
    @PutMapping("/{responseId}")
    public ResponseEntity<APIResponse<ResponseResponseDTO>> updateResponse(
        @PathVariable Long topicId,
        @PathVariable Long responseId,
        @RequestBody @Valid ResponseRequestDTO request, 
        @AuthenticationPrincipal User user) {

        ResponseResponseDTO updatedResponse = responseService.update(responseId, request.content(), user);
        return ResponseEntity.ok(APIResponse.success(updatedResponse));
    }
}