package com.lp3.elearning.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lp3.elearning.entities.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r JOIN FETCH r.student WHERE r.course.id = :courseId ORDER BY r.reviewDate DESC")
    Page<Review> findByCourseIdOrderByReviewDateDesc(Long courseId, Pageable pageable);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId); // Verificar se ja teve avaliação do sacana
    int countByCourseId(Long courseId);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.course.id = :courseId")
    Double getAverageRating(Long courseId);
}