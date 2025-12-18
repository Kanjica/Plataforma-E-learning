package com.lp3.elearning.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.ResponseResponseDTO;
import com.lp3.elearning.dto.TopicRequestDTO;
import com.lp3.elearning.dto.TopicResponseDTO;
import com.lp3.elearning.entities.Topic;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.repository.TopicRepository;

@Service
public class TopicService {

    private final UserService userService;
    private final TopicRepository topicRepository;
    private final CourseService courseService;
    private final ResponseService responseService; 

    public TopicService(TopicRepository topicRepository, CourseService courseService, UserService userService, @Lazy ResponseService responseService){
        this.topicRepository = topicRepository;
        this.courseService = courseService;
        this.userService = userService;
        this.responseService = responseService;
    }

    @Transactional(readOnly = true)
    public List<TopicResponseDTO> findAll() {
        return topicRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TopicResponseDTO> findAllByCourse(Long courseId) {
        return topicRepository.findByCourseId(courseId).stream() 
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TopicResponseDTO findById(Long id) {
        Topic topic = topicRepository.findById(id)
            .orElseThrow(()-> new BusinessRuleException("Tópico não encontrado com id: " + id));
        return toResponseDTO(topic);
    }

    @Transactional
    public TopicResponseDTO create(TopicRequestDTO request, User user) {
        Topic newTopic = Topic.builder()
            .title(request.title())
            .content(request.content())
            .course(courseService.findById(request.courseId()))
            .user(user)
            .responses(Collections.emptySet()) 
            .build();
        
        return toResponseDTO(topicRepository.save(newTopic));
    }

    @Transactional
    public TopicResponseDTO update(Long id, TopicRequestDTO request) {
        Topic existingTopic = topicRepository.findById(id)
                                .orElseThrow(() -> new BusinessRuleException("Tópico não encontrado"));

        existingTopic.setTitle(request.title());
        existingTopic.setContent(request.content());
        
        return toResponseDTO(topicRepository.save(existingTopic));
    }

    @Transactional
    public void delete(Long id) {
        if (!topicRepository.existsById(id)) {
            throw new BusinessRuleException("Tópico não encontrado.");
        }
        topicRepository.deleteById(id);
    }
    
    // Converter
    private TopicResponseDTO toResponseDTO(Topic topic){
        // Filtra apenas as respostas raiz (sem pai) para não duplicar na árvore
        Set<ResponseResponseDTO> responseDTOs = topic.getResponses() == null ? Collections.emptySet() :
            topic.getResponses().stream()
                .filter(response -> response.getResponseParent() == null) 
                .map(responseService::toResponseResponseDTO) 
                .collect(Collectors.toSet());
        
        return new TopicResponseDTO(
            topic.getId(),
            topic.getTitle(),
            topic.getContent(),
            topic.getCreationDate(),
            topic.getCourse().getId(),
            topic.getCourse().getTitle(),
            userService.toResponseDTO(topic.getUser()),
            responseDTOs
        );
    }
}