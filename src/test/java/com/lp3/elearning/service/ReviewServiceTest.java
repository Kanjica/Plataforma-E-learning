package com.lp3.elearning.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lp3.elearning.dto.forum.ReviewRequestDTO;
import com.lp3.elearning.entities.Review;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.repository.CourseRepository;
import com.lp3.elearning.repository.EnrollmentRepository;
import com.lp3.elearning.repository.ReviewRepository;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks private ReviewService reviewService;
    @Mock private ReviewRepository reviewRepository;
    @Mock private EnrollmentRepository enrollmentRepository;
    @Mock private CourseRepository courseRepository;

    @Test
    void shouldPreventReview_IfStudentNotEnrolled() {
        Student student = new Student(); student.setId(1L);
        Long courseId = 10L;

        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 10L)).thenReturn(false);

        assertThrows(BusinessRuleException.class, () -> 
            reviewService.createReview(courseId, new ReviewRequestDTO(5, "Bom"), student));
    }

    @Test
    void shouldPreventDelete_IfNotOwner() {
        Student owner = new Student(); owner.setId(1L);
        Student hacker = new Student(); hacker.setId(2L);
        
        Review review = new Review();
        review.setId(100L);
        review.setStudent(owner);

        when(reviewRepository.findById(100L)).thenReturn(Optional.of(review));

        assertThrows(BusinessRuleException.class, () -> 
            reviewService.deleteReview(1L, 100L, hacker));
    }
}