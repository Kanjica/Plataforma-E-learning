package com.lp3.elearning.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.forum.TopicRequestDTO;
import com.lp3.elearning.dto.forum.TopicResponseDTO;
import com.lp3.elearning.entities.Topic;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.entities.UserRole;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.mapper.TopicMapper;
import com.lp3.elearning.repository.TopicRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    @Transactional(readOnly = true)
    public Page<TopicResponseDTO> findAll(Long courseId, Pageable pageable) {
        Page<Topic> topicPage = (courseId != null)
                ? topicRepository.findByCourseId(courseId, pageable)
                : topicRepository.findAll(pageable);

        return topicPage.map(topicMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public TopicResponseDTO findById(Long id) {
        Topic topic = topicRepository.findById(id)
            .orElseThrow(()-> new BusinessRuleException("Tópico não encontrado com id: " + id));
        return topicMapper.toResponseDTO(topic);
    }

    @Transactional
    public TopicResponseDTO create(TopicRequestDTO request, User user) {

        Topic newTopic = topicMapper.toEntity(request);
        newTopic.setUser(user);
        
        return topicMapper.toResponseDTO(topicRepository.save(newTopic));
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
        
        return topicMapper.toResponseDTO(topicRepository.save(existingTopic));
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
    
}