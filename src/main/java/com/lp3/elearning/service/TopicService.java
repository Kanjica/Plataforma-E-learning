package com.lp3.elearning.service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.forum.ResponseResponseDTO;
import com.lp3.elearning.dto.forum.TopicRequestDTO;
import com.lp3.elearning.dto.forum.TopicResponseDTO;
import com.lp3.elearning.entities.Topic;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.entities.UserRole;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.repository.TopicRepository;

@Service
public class TopicService {

    private final UserService userService;
    private final TopicRepository topicRepository;
    private final CourseService courseService;
    private final ResponseService responseService; 

    public TopicService(TopicRepository topicRepository, CourseService courseService, UserService userService,@Lazy ResponseService responseService){
        this.topicRepository = topicRepository;
        this.courseService = courseService;
        this.userService = userService;
        this.responseService = responseService;
    }

    @Transactional(readOnly = true)
    public Page<TopicResponseDTO> findAll(Long courseId, Pageable pageable) {
        Page<Topic> topicPage = (courseId != null)
                ? topicRepository.findByCourseId(courseId, pageable)
                : topicRepository.findAll(pageable);

        return topicPage.map(this::toResponseDTO);
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
    public TopicResponseDTO update(Long id, TopicRequestDTO request, User user) {

        Topic existingTopic = topicRepository.findById(id)
                                .orElseThrow(() -> new BusinessRuleException("Tópico não encontrado"));

        if(!existingTopic.getUser().equals(user) && !(user.getRole() == UserRole.ROLE_ADMIN)){
            throw new BusinessRuleException("Apenas o autor do tópico podem editá-lo.");
        }

        existingTopic.setTitle(request.title());
        existingTopic.setContent(request.content());
        
        return toResponseDTO(topicRepository.save(existingTopic));
    }

    @Transactional
    public void delete(Long topicId, User user) {
        Topic topic = topicRepository.findById(topicId)
            .orElseThrow(() -> new BusinessRuleException("Tópico não encontrado."));

        if(!topic.getUser().equals(user) && !(user.getRole() == UserRole.ROLE_ADMIN)){
            throw new BusinessRuleException("Apenas o autor do tópico podem excluí-lo.");
        }

        topicRepository.deleteById(topicId);
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