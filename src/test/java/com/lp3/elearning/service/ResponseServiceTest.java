package com.lp3.elearning.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lp3.elearning.dto.forum.ResponseRequestDTO;
import com.lp3.elearning.entities.Response;
import com.lp3.elearning.entities.Topic;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.repository.ResponseRepository;
import com.lp3.elearning.repository.TopicRepository;

@ExtendWith(MockitoExtension.class)
class ResponseServiceTest {

    @InjectMocks private ResponseService responseService;
    @Mock private ResponseRepository responseRepository;
    @Mock private TopicRepository topicRepository;
    @Mock private UserService userService;

    @Test
    void shouldThrowException_WhenReplyToWrongTopic() {
        // Tópico A
        Topic topicA = new Topic(); topicA.setId(1L);
        
        // Resposta Pai está no Tópico B
        Topic topicB = new Topic(); topicB.setId(2L);
        Response parent = new Response(); parent.setId(50L); parent.setTopic(topicB);

        ResponseRequestDTO request = new ResponseRequestDTO("Reply", 1L, 50L); // Tenta responder no topico A com pai do B

        when(topicRepository.findById(1L)).thenReturn(Optional.of(topicA));
        when(responseRepository.findById(50L)).thenReturn(Optional.of(parent));

        assertThrows(BusinessRuleException.class, () -> responseService.create(request, new User()));
    }
}