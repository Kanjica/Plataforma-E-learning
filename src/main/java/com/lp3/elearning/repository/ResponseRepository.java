package com.lp3.elearning.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lp3.elearning.entities.Response;

public interface ResponseRepository extends JpaRepository<Response, Long>{
    List<Response> findByTopicId(Long topicId);
    
    // Busca apenas as respostas principais do tópico (nível 0)
    Page<Response> findByTopicIdAndResponseParentIsNull(Long topicId, Pageable pageable);

    // Busca as "filhas" de uma resposta específica (próximo nível)
    List<Response> findByResponseParentId(Long parentId);
}
