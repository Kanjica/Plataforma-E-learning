package com.lp3.elearning.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.lp3.elearning.entities.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    boolean existsByIdAndStudentId(Long enrollmentId, Long studentId);
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);
    List<Enrollment> findByStudentIdOrderByEnrollmentDateDesc(Long studentId);
    List<Enrollment> findByStudentId(Long studentId);
    Page<Enrollment> findByStudentId(Long studentId, Pageable pageable);
}
