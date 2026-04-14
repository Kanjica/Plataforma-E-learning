package com.lp3.elearning.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.forum.ResponseRequestDTO;
import com.lp3.elearning.dto.forum.ResponseResponseDTO;
import com.lp3.elearning.entities.Response;
import com.lp3.elearning.entities.Topic;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.mapper.ResponseMapper;
import com.lp3.elearning.repository.ResponseRepository;
import com.lp3.elearning.repository.TopicRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final TopicRepository topicRepository;
    private final ResponseMapper responseMapper;

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
            .build();
        
        return responseMapper.toResponseDTO(responseRepository.save(newResponse));
    }

    @Transactional(readOnly = true)
    public Page<ResponseResponseDTO> findRootResponsesByTopic(Long topicId, Pageable pageable) {
        // 1. Busca do banco já paginada
        Page<Response> roots = responseRepository.findByTopicIdAndResponseParentIsNull(topicId, pageable);
        
        // 2. O próprio objeto Page tem o método .map() que funciona perfeitamente com o MapStruct
        return roots.map(responseMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public List<ResponseResponseDTO> findChildren(Long parentId) {
        // Para as "filhas", geralmente não paginamos (a menos que a thread seja gigante)
        return responseRepository.findByResponseParentId(parentId).stream()
            .map(responseMapper::toResponseDTO)
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
        return responseMapper.toResponseDTO(responseRepository.save(response));
    }

}