package com.lp3.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lp3.elearning.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByTitle(String title);
}
