package com.lp3.elearning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lp3.elearning.entities.Response;

public interface ResponseRepository extends JpaRepository<Response, Long>{
    List<Response> findByTopicIdAndResponseParentIsNull(Long topicId);

    List<Response> findByTopicId(Long topicId);
}
