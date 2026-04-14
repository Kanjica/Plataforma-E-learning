package com.lp3.elearning.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.forum.ReviewRequestDTO;
import com.lp3.elearning.dto.forum.ReviewResponseDTO;
import com.lp3.elearning.entities.Course;
import com.lp3.elearning.entities.Review;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.entities.UserRole;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.exception.ConflictException;
import com.lp3.elearning.exception.ResourceNotFoundException;
import com.lp3.elearning.mapper.ReviewMapper;
import com.lp3.elearning.repository.CourseRepository;
import com.lp3.elearning.repository.EnrollmentRepository;
import com.lp3.elearning.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final EnrollmentRepository enrollmentRepository;
    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final ReviewMapper reviewMapper;

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

        updateCourseStats(courseId);
        
        return reviewMapper.toResponseDTO(reviewRepository.save(review));
    }

    private void updateCourseStats(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        
        // Busca a média e o total direto do Repository de Reviews
        Double avg = reviewRepository.getAverageRating(courseId);
        Integer total = reviewRepository.countByCourseId(courseId);
        
        course.setAverageRating(BigDecimal.valueOf(avg != null ? avg : 0.0));
        course.setTotalReviews(total);
        
        courseRepository.save(course);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDTO> listByCourse(Long courseId, Pageable pageable) {
        return reviewRepository.findByCourseIdOrderByReviewDateDesc(courseId, pageable)
                .map(reviewMapper::toResponseDTO);
    }

    @Transactional
    public ReviewResponseDTO updateReview(Long reviewId, ReviewRequestDTO request, Student student) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada"));

        if (!review.getStudent().getId().equals(student.getId())) {
            throw new BusinessRuleException("Você não tem permissão para editar esta avaliação.");
        }

        review.setRating(request.rating());
        review.setComment(request.comment());
        // Data de atualização poderia ser setada aqui se tiver campo updatedAt

        return reviewMapper.toResponseDTO(reviewRepository.save(review));
    }

    @Transactional
    public void deleteReview(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada"));

        if (!review.getStudent().getId().equals(user.getId()) && user.getRole() == UserRole.ROLE_STUDENT) {
            throw new BusinessRuleException("Você não tem permissão para remover esta avaliação.");
        }

        reviewRepository.delete(review);
    }
}