package com.lp3.elearning.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.ResponseRequestDTO;
import com.lp3.elearning.dto.ResponseResponseDTO;
import com.lp3.elearning.dto.UserResponseDTO;
import com.lp3.elearning.entities.Response;
import com.lp3.elearning.entities.Topic;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.repository.ResponseRepository;
import com.lp3.elearning.repository.TopicRepository;

@Service
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final TopicRepository topicRepository;
    private final UserService userService;

    public ResponseService(ResponseRepository responseRepository, TopicRepository topicRepository, UserService userService) {
        this.responseRepository = responseRepository;
        this.topicRepository = topicRepository;
        this.userService = userService;
    }

    /**
     * Cria uma resposta em um tópico ou um reply para outra resposta.
     */
    @Transactional
    public ResponseResponseDTO create(ResponseRequestDTO request, User user) {
        Topic topic = topicRepository.findById(request.topicId())
            .orElseThrow(() -> new BusinessRuleException("Tópico não encontrado com ID: " + request.topicId()));

        Response parentResponse = null;
        if (request.responseParentId() != null) {
            parentResponse = responseRepository.findById(request.responseParentId())
                .orElseThrow(() -> new BusinessRuleException("Resposta pai não encontrada."));
            
            // Integridade: Reply deve ser do mesmo tópico
            if (!parentResponse.getTopic().getId().equals(topic.getId())) {
                throw new BusinessRuleException("Erro de integridade: A resposta pai pertence a outro tópico.");
            }
        }

        Response newResponse = Response.builder()
            .content(request.content())
            .topic(topic)
            .user(user)
            .responseParent(parentResponse)
            .creationDate(LocalDateTime.now())
            .build();
        
        return toResponseResponseDTO(responseRepository.save(newResponse));
    }

    @Transactional(readOnly = true)
    public List<ResponseResponseDTO> findRootResponsesByTopic(Long topicId) {
        return responseRepository.findByTopicIdAndResponseParentIsNull(topicId).stream()
            .map(this::toResponseResponseDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public void delete(Long responseId, User user) {
        Response response = responseRepository.findById(responseId)
            .orElseThrow(() -> new BusinessRuleException("Resposta não encontrada"));

        // Validação de Dono ou Admin poderia entrar aqui
        if (!response.getUser().getId().equals(user.getId())){
            throw new BusinessRuleException("Você não tem permissão para deletar esta resposta.");
        }
        responseRepository.delete(response);
    }

    @Transactional
    public ResponseResponseDTO update(Long responseId, String newContent, User user) {
        Response response = responseRepository.findById(responseId)
            .orElseThrow(() -> new BusinessRuleException("Resposta não encontrada"));

        if (!response.getUser().getId().equals(user.getId())) {
            throw new BusinessRuleException("Você não tem permissão para editar esta resposta.");
        }
        
        response.setContent(newContent);
        return toResponseResponseDTO(responseRepository.save(response));
    }

    // Conversão Recursiva para montar a árvore de comentários
    public ResponseResponseDTO toResponseResponseDTO(Response response) {
        List<ResponseResponseDTO> childDTOs = response.getChildResponses() == null ? List.of() :
            response.getChildResponses().stream()
                .map(this::toResponseResponseDTO) // Recursão
                .collect(Collectors.toList());

        UserResponseDTO userDTO = userService.toResponseDTO(response.getUser());
        
        return new ResponseResponseDTO(
            response.getId(),
            response.getContent(),
            response.getCreationDate(),
            userDTO,
            response.getTopic().getId(),
            response.getResponseParent() != null ? response.getResponseParent().getId() : null,
            childDTOs
        );
    }
}