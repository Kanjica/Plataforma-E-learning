package com.lp3.elearning.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.lp3.elearning.dto.TopicRequestDTO;
import com.lp3.elearning.dto.TopicResponseDTO;
import com.lp3.elearning.dto.ResponseResponseDTO; // Importado
import com.lp3.elearning.entities.Topic;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.repository.TopicRepository;
import com.lp3.elearning.repository.UserRepository;

@Service
public class TopicService {

    private final UserService userService;
    private final TopicRepository topicRepository;
    private final CourseService courseService;
    private final UserRepository userRepository;
    private final ResponseService responseService; // Novo: Para conversão de Responses

    public TopicService(TopicRepository topicRepository, CourseService courseService, UserRepository userRepository, UserService userService, ResponseService responseService){
        this.topicRepository = topicRepository;
        this.courseService = courseService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.responseService = responseService;
    }

    private TopicResponseDTO toResponseDTO(Topic topic){

        // Mapeia o Set de Responses para ResponseResponseDTOs usando o ResponseService
        Set<ResponseResponseDTO> responseDTOs = topic.getResponses().stream()
                .filter(response -> response.getResponseParent() == null) // Filtra apenas respostas de nível superior (raízes)
                .map(responseService::toResponseResponseDTO) // Assumindo que este método é público no ResponseService
                .collect(Collectors.toSet());
        
        return new TopicResponseDTO(
            topic.getId(),
            topic.getTitle(),
            topic.getContent(),
            topic.getCreationDate(),
            courseService.toResponseDTO(topic.getCourse()),
            userService.toResponseDTO(topic.getUser()),
            responseDTOs // Agora inclui as responses raízes
        );
    }
    
    public List<TopicResponseDTO> findAll() {
        return topicRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // NOVO: Busca de tópicos filtrados por curso
    public List<TopicResponseDTO> findAllByCourse(Long courseId) {
        // Requer o método findByCourseId no TopicRepository
        return topicRepository.findByCourseId(courseId).stream() 
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TopicResponseDTO findById(Long id) {
        Topic topic = topicRepository.findById(id)
            .orElseThrow(()-> new BusinessRuleException("Topic not found with id: " + id));
        return toResponseDTO(topic);
    }

    public TopicResponseDTO create(TopicRequestDTO request) {
        
        // Em um sistema real, o user ID viria do token de autenticação, não do DTO.
        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> new BusinessRuleException("User not found with ID: " + request.userId()));
        
        Topic newTopic = Topic.builder()
            .title(request.title())
            .content(request.content())
            .course(courseService.findById(request.courseId()))
            .user(user)
            .responses(Collections.emptySet()) 
            .build();
        
        return toResponseDTO(topicRepository.save(newTopic));
    }

    public TopicResponseDTO update(Long id, TopicRequestDTO request) {
        Topic existingTopic = topicRepository.findById(id)
                                .orElseThrow(() -> new BusinessRuleException("Id de tópico n encontrado"));

        // ** AQUI DEVERIA TER UMA VALIDAÇÃO DE AUTORIZAÇÃO ** // if (usuarioAutenticadoNaoPodeEditar(existingTopic)) { throw new BusinessRuleException(...) }

        existingTopic.setTitle(request.title());
        existingTopic.setContent(request.content());
        
        Topic updatedTopic = topicRepository.save(existingTopic);
        return toResponseDTO(updatedTopic);
    }

    public void delete(Long id) {
        // ** AQUI DEVERIA TER UMA VALIDAÇÃO DE AUTORIZAÇÃO **
        Topic topic = topicRepository.findById(id)
            .orElseThrow(() -> new BusinessRuleException("Topic not found with id: " + id));
        
        topicRepository.delete(topic);
    }
}