package com.lp3.elearning.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.ReviewRequestDTO;
import com.lp3.elearning.dto.ReviewResponseDTO;
import com.lp3.elearning.entities.Course;
import com.lp3.elearning.entities.Review;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.entities.User;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.exception.ResourceNotFoundException;
import com.lp3.elearning.repository.CourseRepository;
import com.lp3.elearning.repository.ReviewRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;

    public ReviewService(ReviewRepository reviewRepository, CourseRepository courseRepository) {
        this.reviewRepository = reviewRepository;
        this.courseRepository = courseRepository;
    }

    public ReviewResponseDTO createReview(Long courseId, ReviewRequestDTO request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(user instanceof Student student)) {
            throw new BusinessRuleException("Apenas alunos podem avaliar cursos.");
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
}