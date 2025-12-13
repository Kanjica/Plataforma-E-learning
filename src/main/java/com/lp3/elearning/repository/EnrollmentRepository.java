package com.lp3.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lp3.elearning.entities.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
}
