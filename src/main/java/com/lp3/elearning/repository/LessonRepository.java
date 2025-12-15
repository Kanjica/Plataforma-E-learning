package com.lp3.elearning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lp3.elearning.entities.Lesson;;

public interface LessonRepository extends JpaRepository<Lesson, Long>{
    List<Lesson> findByModuleId(Long moduleId);
}
