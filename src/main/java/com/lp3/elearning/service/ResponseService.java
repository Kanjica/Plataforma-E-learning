package com.lp3.elearning.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.ResponseRequestDTO;
import com.lp3.elearning.dto.ResponseResponseDTO;
import com.lp3.elearning.dto.UserResponseDTO;
import com.lp3.elearning.entities.Response;
import com.lp3.elearning.entities.Topic;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.repository.ResponseRepository;
import com.lp3.elearning.repository.TopicRepository;

import jakarta.transaction.Transactional;

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

    // Método de Conversão de Entidade para DTO (Pode ser chamado recursivamente para montar o thread)
    public ResponseResponseDTO toResponseResponseDTO(Response response) {
        
        // Conversão dos filhos (respostas aninhadas)
        List<ResponseResponseDTO> childDTOs = response.getChildResponses().stream()
            .collect(Collectors.toUnmodifiableList()) // Garante a cópia
            .stream()
            .map(this::toResponseResponseDTO) 
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
    
    // Cria uma nova resposta ou reply
    public ResponseResponseDTO create(ResponseRequestDTO request, User user) {
        
        Topic topic = topicRepository.findById(request.topicId())
            .orElseThrow(() -> new BusinessRuleException("Tópico não encontrado com ID: " + request.topicId()));

        Response parentResponse = null;
        if (request.responseParentId() != null) {
            parentResponse = responseRepository.findById(request.responseParentId())
                .orElseThrow(() -> new BusinessRuleException("Resposta pai não encontrada com ID: " + request.responseParentId()));
            
            // Validação extra: O reply deve pertencer ao mesmo tópico do pai
            if (!parentResponse.getTopic().getId().equals(topic.getId())) {
                throw new BusinessRuleException("A resposta pai pertence a outro tópico.");
            }
        }

        Response newResponse = Response.builder()
            .content(request.content())
            .topic(topic)
            .user(user)
            .responseParent(parentResponse)
            .build();
        
        return toResponseResponseDTO(responseRepository.save(newResponse));
    }

    // Busca apenas as respostas de "primeiro nível" para um tópico (raízes)
    public List<ResponseResponseDTO> findRootResponsesByTopic(Long topicId) {
        // Requer o método findByTopicIdAndResponseParentIsNull no ResponseRepository
        List<Response> rootResponses = responseRepository.findByTopicIdAndResponseParentIsNull(topicId);
        
        return rootResponses.stream()
            .map(this::toResponseResponseDTO)
            .collect(Collectors.toList());
    }
    
    public void delete(Long responseId, User user) {
        Response response = responseRepository.findById(responseId)
            .orElseThrow(() -> new BusinessRuleException("Resposta não encontrada"));

        boolean isAuthor = response.getUser().getId().equals(user.getId());
        
        if (!isAuthor){
            throw new BusinessRuleException("Sem permissão para deletar esta resposta.");
        }

        responseRepository.delete(response);
    }

    @Transactional
    public ResponseResponseDTO update(Long responseId, String newContent, User user) {
        Response response = responseRepository.findById(responseId)
            .orElseThrow(() -> new BusinessRuleException("Resposta não encontrada com ID: " + responseId));

        // VALIDAÇÃO DE SEGURANÇA: Somente o autor pode editar
        if (!response.getUser().getId().equals(user.getId())) {
            throw new BusinessRuleException("Você não tem permissão para editar esta resposta.");
        }

        // Validação de conteúdo (opcional, se não estiver usando @Valid no DTO)
        if (newContent == null || newContent.trim().isEmpty()) {
            throw new BusinessRuleException("O conteúdo da resposta não pode estar vazio.");
        }

        response.setContent(newContent);
        
        // O save aqui é opcional se o método for @Transactional, 
        // mas ajuda na clareza e retorna o objeto atualizado
        return toResponseResponseDTO(responseRepository.save(response));
    }
}