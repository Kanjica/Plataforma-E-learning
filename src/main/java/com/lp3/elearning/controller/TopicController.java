package com.lp3.elearning.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lp3.elearning.dto.TopicRequestDTO;
import com.lp3.elearning.dto.TopicResponseDTO;
import com.lp3.elearning.entities.Instructor;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.service.TopicService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/topics")
public class TopicController {
    
    private final TopicService topicService;

    // Apenas TopicService é injetado, o Repository deve ser gerenciado pelo Service
    public TopicController(TopicService topicService){ 
        this.topicService = topicService;
    }

    // [1] Listar Tópicos (Global ou Filtrado por Curso)
    // GET /topics                 -> Lista todos
    // GET /topics?courseId=123    -> Lista por curso
    @GetMapping
    public ResponseEntity<List<TopicResponseDTO>> getAllTopics(@RequestParam(required = false) Long courseId) {
        if (courseId != null) {
            return ResponseEntity.ok(topicService.findAllByCourse(courseId)); 
        }
        return ResponseEntity.ok(topicService.findAll());
    }
    
    // [2] Criar um novo Tópico
    // POST /topics (O courseId e userId vêm no corpo do DTO)
    @PostMapping
    public ResponseEntity<TopicResponseDTO> createTopic(
        @RequestBody @Valid TopicRequestDTO request,
        @AuthenticationPrincipal User user) {
        TopicResponseDTO response = topicService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    // [3] Obter um Tópico por ID
    // GET /topics/{topicId}
    @GetMapping("/{topicId}")
    public ResponseEntity<TopicResponseDTO> getTopicById(@PathVariable Long topicId) {
        TopicResponseDTO topic = topicService.findById(topicId);
        return ResponseEntity.ok(topic);
    }

    // [4] Atualizar um Tópico
    // PUT /topics/{topicId}
    @PutMapping("/{topicId}")
    public ResponseEntity<TopicResponseDTO> updateTopic(
        @PathVariable Long topicId, 
        @RequestBody @Valid TopicRequestDTO request,
        @AuthenticationPrincipal Instructor instructor) {
        // O service deve validar se o usuário autenticado é o criador ou um instrutor/admin
        TopicResponseDTO response = topicService.update(topicId, request, instructor);
        return ResponseEntity.ok(response);
    }

    // [5] Deletar um Tópico
    // DELETE /topics/{topicId}
    @DeleteMapping("/{topicId}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long topicId) {
        // O service deve validar se o usuário autenticado é o criador ou um instrutor/admin
        topicService.delete(topicId);
        return ResponseEntity.noContent().build();
    }
}