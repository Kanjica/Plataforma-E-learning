package com.lp3.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lp3.elearning.entities.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long>{
    
}
