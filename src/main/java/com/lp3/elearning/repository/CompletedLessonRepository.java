package com.lp3.elearning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lp3.elearning.entities.CompletedLesson;
import com.lp3.elearning.entities.Enrollment;

public interface CompletedLessonRepository extends JpaRepository<CompletedLesson, Long> {
    public Integer countByEnrollment(Enrollment enrollment);
    public List<CompletedLesson> findByEnrollment(Enrollment enrollment);
}
