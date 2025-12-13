package com.lp3.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lp3.elearning.entities.CompletedLesson;

public interface CompletedLessonRepository extends JpaRepository<CompletedLesson, Long> {
    
}
