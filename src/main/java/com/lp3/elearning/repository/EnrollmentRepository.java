package com.lp3.elearning.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.lp3.elearning.entities.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    boolean existsByIdAndStudentId(Long enrollmentId, Long studentId);
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);
    List<Enrollment> findByStudentIdOrderByEnrollmentDateDesc(Long studentId);
    List<Enrollment> findByStudentId(Long studentId);
    Page<Enrollment> findByStudentId(Long studentId, Pageable pageable);
    @Query("""
        SELECT (
            (SELECT COUNT(cl) FROM CompletedLesson cl WHERE cl.enrollment.id = :enrollmentId) * 1.0 / 
            (SELECT COUNT(l) FROM Lesson l WHERE l.module.course.id = :courseId)
        ) 
        FROM Enrollment e WHERE e.id = :enrollmentId
    """)
    Double getProgress(@Param("enrollmentId") Long enrollmentId, @Param("courseId") Long courseId);
}
