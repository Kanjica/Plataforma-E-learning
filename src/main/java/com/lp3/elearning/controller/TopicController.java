package com.lp3.elearning.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lp3.elearning.dto.common.APIResponse;
import com.lp3.elearning.dto.forum.TopicRequestDTO;
import com.lp3.elearning.dto.forum.TopicResponseDTO;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.security.anottation.CurrentUser;
import com.lp3.elearning.service.TopicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/topics")
@Tag(name = "Fórum - Tópicos", description = "Gestão de tópicos de discussão dos cursos")
public class TopicController {
    
    private final TopicService topicService;

    public TopicController(TopicService topicService){ 
        this.topicService = topicService;
    }

    @Operation(summary = "Listar Tópicos", description = "Retorna tópicos paginados, com filtro opcional por curso")
    @GetMapping
    public ResponseEntity<APIResponse<Page<TopicResponseDTO>>> getAllTopics(
            @RequestParam(required = false) Long courseId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(APIResponse.success(topicService.findAll(courseId, pageable)));
    }
    
    @Operation(summary = "Criar Tópico", description = "Abre uma nova discussão no fórum")
    @PostMapping
    public ResponseEntity<APIResponse<TopicResponseDTO>> createTopic(
            @RequestBody @Valid TopicRequestDTO request,
            @CurrentUser User user
    ) {
        TopicResponseDTO response = topicService.create(request, user);
        return ResponseEntity.ok(APIResponse.success(response));
    }
    
    @Operation(summary = "Buscar Tópico por ID", description = "Exibe os detalhes de um tópico específico")
    @GetMapping("/{topicId}")
    public ResponseEntity<APIResponse<TopicResponseDTO>> getTopicById(@PathVariable Long topicId) {
        return ResponseEntity.ok(APIResponse.success(topicService.findById(topicId)));
    }

    @Operation(summary = "Atualizar Tópico", description = "Permite editar o título ou mensagem do tópico")
    @PutMapping("/{topicId}")
    public ResponseEntity<APIResponse<TopicResponseDTO>> updateTopic(
            @PathVariable Long topicId, 
            @RequestBody @Valid TopicRequestDTO request,
            @CurrentUser User user
    ) {
        return ResponseEntity.ok(APIResponse.success(topicService.update(topicId, request, user)));
    }

    @Operation(summary = "Deletar Tópico", description = "Remove o tópico e todas as suas respostas")
    @DeleteMapping("/{topicId}")
    public ResponseEntity<APIResponse<Void>> deleteTopic(
            @PathVariable Long topicId,
            @CurrentUser User user
    ) {
        topicService.delete(topicId, user);
        return ResponseEntity.ok(APIResponse.success(null));
    }
}