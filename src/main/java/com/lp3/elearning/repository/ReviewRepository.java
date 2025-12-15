package com.lp3.elearning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lp3.elearning.entities.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCourseIdOrderByReviewDateDesc(Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId); // Verificar se ja teve avaliação do sacana
}