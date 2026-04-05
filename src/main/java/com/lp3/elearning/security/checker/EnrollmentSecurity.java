package com.lp3.elearning.security.checker;

import org.springframework.stereotype.Component;
import com.lp3.elearning.repository.EnrollmentRepository;

@Component("enrollmentSecurity")
public class EnrollmentSecurity {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentSecurity(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public boolean isOwner(Long enrollmentId, Long userId) {
        return enrollmentRepository.existsByIdAndStudentId(enrollmentId, userId);
    }

}