package com.lp3.elearning.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.lp3.elearning.dto.forum.TopicRequestDTO;
import com.lp3.elearning.dto.forum.TopicResponseDTO;
import com.lp3.elearning.entities.Instructor;
import com.lp3.elearning.entities.User;
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

    @Operation(summary = "Listar Tópicos", description = "Retorna todos os tópicos ou filtra por curso se 'courseId' for informado")
    @GetMapping
    public ResponseEntity<List<TopicResponseDTO>> getAllTopics(@RequestParam(required = false) Long courseId) {
        if (courseId != null) {
            return ResponseEntity.ok(topicService.findAllByCourse(courseId)); 
        }
        return ResponseEntity.ok(topicService.findAll());
    }
    
    @Operation(summary = "Criar Tópico", description = "Abre uma nova discussão no fórum")
    @PostMapping
    public ResponseEntity<TopicResponseDTO> createTopic(
        @RequestBody @Valid TopicRequestDTO request,
        @AuthenticationPrincipal User user) {
        TopicResponseDTO response = topicService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Buscar Tópico por ID", description = "Exibe os detalhes de um tópico específico")
    @GetMapping("/{topicId}")
    public ResponseEntity<TopicResponseDTO> getTopicById(@PathVariable Long topicId) {
        return ResponseEntity.ok(topicService.findById(topicId));
    }

    @Operation(summary = "Atualizar Tópico", description = "Permite editar o título ou mensagem do tópico")
    @PutMapping("/{topicId}")
    public ResponseEntity<TopicResponseDTO> updateTopic(
        @PathVariable Long topicId, 
        @RequestBody @Valid TopicRequestDTO request,
        @AuthenticationPrincipal Instructor instructor) {
        return ResponseEntity.ok(topicService.update(topicId, request));
    }

    @Operation(summary = "Deletar Tópico", description = "Remove o tópico e todas as suas respostas")
    @DeleteMapping("/{topicId}")
    public ResponseEntity<Void> deleteTopic(
        @PathVariable Long topicId,
        @AuthenticationPrincipal Instructor instructor) {
        topicService.delete(topicId);
        return ResponseEntity.noContent().build();
    }
}