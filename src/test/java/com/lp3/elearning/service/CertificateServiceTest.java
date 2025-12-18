package com.lp3.elearning.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.entities.StatusEnrollment;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.exception.BusinessRuleException;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {

    @InjectMocks private CertificateService certificateService;
    @Mock private EnrollmentService enrollmentService;

    @Test
    void shouldDenyCertificate_WhenNotCompleted() {
        Student student = new Student(); student.setId(1L);
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setStatus(StatusEnrollment.IN_PROGRESS);
        enrollment.setOverallProgress(0.99); // 99%

        when(enrollmentService.findById(1L)).thenReturn(enrollment);

        assertThrows(BusinessRuleException.class, () -> 
            certificateService.generateCertificatePdf(1L, student));
    }

    @Test
    void shouldDenyCertificate_WhenWrongUser() {
        Student owner = new Student(); owner.setId(1L);
        Student hacker = new Student(); hacker.setId(2L);
        
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(owner);

        when(enrollmentService.findById(1L)).thenReturn(enrollment);

        assertThrows(BusinessRuleException.class, () -> 
            certificateService.generateCertificatePdf(1L, hacker));
    }
}