package com.lp3.elearning.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.forum.ReviewRequestDTO;
import com.lp3.elearning.dto.forum.ReviewResponseDTO;
import com.lp3.elearning.entities.Course;
import com.lp3.elearning.entities.Review;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.exception.ConflictException;
import com.lp3.elearning.exception.ResourceNotFoundException;
import com.lp3.elearning.repository.CourseRepository;
import com.lp3.elearning.repository.EnrollmentRepository;
import com.lp3.elearning.repository.ReviewRepository;

@Service
public class ReviewService {

    private final EnrollmentRepository enrollmentRepository;
    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;

    public ReviewService(ReviewRepository reviewRepository, CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.reviewRepository = reviewRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    /**
     * Cria uma nova avaliação para um curso.
     * @throws BusinessRuleException se o aluno não for matriculado ou já tiver avaliado.
     */
    @Transactional
    public ReviewResponseDTO createReview(Long courseId, ReviewRequestDTO request, Student student) {
        if(!enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)){
            throw new BusinessRuleException("Acesso negado: Você precisa estar matriculado para avaliar este curso.");
        }

        Course course = courseRepository.findById(courseId) 
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));

        if (reviewRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            throw new ConflictException("Você já avaliou este curso.");
        }

        Review review = Review.builder()
                .rating(request.rating())
                .comment(request.comment())
                .student(student)
                .course(course)
                .reviewDate(LocalDateTime.now()) // Garante a data atual
                .build();

        return toDTO(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> listByCourse(Long courseId) {
        return reviewRepository.findByCourseIdOrderByReviewDateDesc(courseId).stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public ReviewResponseDTO updateReview(Long courseId, Long reviewId, ReviewRequestDTO request, Student student) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada"));

        if (!review.getCourse().getId().equals(courseId)) {
            throw new BusinessRuleException("A avaliação não pertence ao curso informado.");
        }
        
        if (!review.getStudent().getId().equals(student.getId())) {
            throw new BusinessRuleException("Você não tem permissão para editar esta avaliação.");
        }

        review.setRating(request.rating());
        review.setComment(request.comment());
        // Data de atualização poderia ser setada aqui se tiver campo updatedAt

        return toDTO(reviewRepository.save(review));
    }

    @Transactional
    public void deleteReview(Long courseId, Long reviewId, Student student) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada"));

        if (!review.getStudent().getId().equals(student.getId())) {
            throw new BusinessRuleException("Você não tem permissão para remover esta avaliação.");
        }

        reviewRepository.delete(review);
    }

    private ReviewResponseDTO toDTO(Review review) {
        return new ReviewResponseDTO(
            review.getId(),
            review.getStudent().getName(),
            review.getRating(),
            review.getComment(),
            review.getReviewDate()
        );
    }
}