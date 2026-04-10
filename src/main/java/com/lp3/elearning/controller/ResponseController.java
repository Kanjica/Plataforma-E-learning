package com.lp3.elearning.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.dto.forum.ResponseRequestDTO;
import com.lp3.elearning.dto.forum.ResponseResponseDTO;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.security.anottation.CurrentUser;
import com.lp3.elearning.service.ResponseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/responses")
@Tag(name = "Fórum - Respostas", description = "Gestão de comentários e replies nos tópicos")
public class ResponseController {

    private final ResponseService responseService;

    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }
    
    @Operation(summary = "Criar Resposta", description = "Adiciona um comentário ou resposta a um tópico")
    @PostMapping
    public ResponseEntity<APIResponse<ResponseResponseDTO>> createResponse(
            @RequestBody @Valid ResponseRequestDTO request,
            @CurrentUser User user
    ){
        var createdResponse = responseService.create(request, user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdResponse.id())
            .toUri();

        return ResponseEntity.created(location).body(APIResponse.success(createdResponse));
    }

    @Operation(summary = "Listar Respostas", description = "Retorna a árvore de comentários de um tópico")
    @GetMapping("/topic/{topicId}")
    public ResponseEntity<APIResponse<List<ResponseResponseDTO>>> getRootResponsesByTopic(@PathVariable Long topicId) {
        return ResponseEntity.ok(APIResponse.success(responseService.findRootResponsesByTopic(topicId)));
    }
    
    @Operation(summary = "Deletar Resposta", description = "Remove um comentário (apenas autor ou admin)")
    @DeleteMapping("/{responseId}")
    public ResponseEntity<APIResponse<Void>> deleteResponse(
            @PathVariable Long responseId,
            @CurrentUser User user
    ) {
        responseService.delete(responseId, user);
        return ResponseEntity.ok(APIResponse.success(null));
    }
    
    @Operation(summary = "Atualizar Resposta", description = "Edita o conteúdo de um comentário")
    @PutMapping("/{responseId}")
    public ResponseEntity<APIResponse<ResponseResponseDTO>> updateResponse(
            @PathVariable Long responseId,
            @RequestBody @Valid ResponseRequestDTO request, 
            @CurrentUser User user
    ) {
        return ResponseEntity.ok(APIResponse.success(responseService.update(responseId, request.content(), user)));
    }
}