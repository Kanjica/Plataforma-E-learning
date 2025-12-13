package com.lp3.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lp3.elearning.entities.Lesson;;

public interface LessonRepository extends JpaRepository<Lesson, Long>{
    
}
