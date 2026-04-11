package com.lp3.elearning.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lp3.elearning.entities.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByCourseIdOrderByReviewDateDesc(Long courseId, Pageable pageable); 
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId); // Verificar se ja teve avaliação do sacana
    
}