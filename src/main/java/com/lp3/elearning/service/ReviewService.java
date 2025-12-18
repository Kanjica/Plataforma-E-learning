package com.lp3.elearning.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.ReviewRequestDTO;
import com.lp3.elearning.dto.ReviewResponseDTO;
import com.lp3.elearning.entities.Course;
import com.lp3.elearning.entities.Review;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.exception.ResourceNotFoundException;
import com.lp3.elearning.repository.CourseRepository;
import com.lp3.elearning.repository.EnrollmentRepository;
import com.lp3.elearning.repository.ReviewRepository;

import jakarta.transaction.Transactional;

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

    public ReviewResponseDTO createReview(Long courseId, ReviewRequestDTO request, Student student) {

        if(!enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)){
            throw new BusinessRuleException("Nem cadastrado c ta irmão");
        }
        // Use o courseId passado no parâmetro, não o do request
        Course course = courseRepository.findById(courseId) 
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado"));

        if (reviewRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            throw new BusinessRuleException("Você já avaliou este curso.");
        }

        Review review = Review.builder()
                .rating(request.rating())
                .comment(request.comment())
                .student(student)
                .course(course)
                .build();

        review = reviewRepository.save(review);

        return new ReviewResponseDTO(
                review.getId(),
                student.getName(),
                review.getRating(),
                review.getComment(),
                review.getReviewDate()
        );
    }

    public List<ReviewResponseDTO> listByCourse(Long courseId) {
        return reviewRepository.findByCourseIdOrderByReviewDateDesc(courseId).stream()
                .map(r -> new ReviewResponseDTO(
                        r.getId(),
                        r.getStudent().getName(),
                        r.getRating(),
                        r.getComment(),
                        r.getReviewDate()
                ))
                .toList();
    }

    @Transactional
public ReviewResponseDTO updateReview(Long courseId, Long reviewId, ReviewRequestDTO request, Student student) {
    Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada"));

    // Validação de segurança: A review pertence a esse curso e a esse aluno?
    if (!review.getCourse().getId().equals(courseId) || !review.getStudent().getId().equals(student.getId())) {
        throw new BusinessRuleException("Você não tem permissão para editar esta avaliação.");
    }

    review.setRating(request.rating());
    review.setComment(request.comment());

    review = reviewRepository.save(review);

    return new ReviewResponseDTO(
            review.getId(),
            student.getName(),
            review.getRating(),
            review.getComment(),
            review.getReviewDate()
    );
}

    @Transactional
    public void deleteReview(Long courseId, Long reviewId, Student student) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada"));

        // Apenas o dono da avaliação pode deletar
        if (!review.getStudent().getId().equals(student.getId())) {
            throw new BusinessRuleException("Você não tem permissão para remover esta avaliação.");
        }

        reviewRepository.delete(review);
    }
}