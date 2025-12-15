package com.lp3.elearning.service;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.EnrollmentRequestDTO;
import com.lp3.elearning.dto.EnrollmentResponseDTO;
import com.lp3.elearning.entities.Course;
import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.repository.EnrollmentRepository;

@Service
public class EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final StudentService studentService;
    private final CourseService courseService;
    private final CompletedLessonsService completedLessonsService;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, StudentService studentService, CourseService courseService, CompletedLessonsService completedLessonsService) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentService = studentService;
        this.courseService = courseService;
        this.completedLessonsService = completedLessonsService;
    }

    public EnrollmentResponseDTO create(EnrollmentRequestDTO request){
        if(enrollmentRepository.existsByStudentIdAndCourseId(request.studentId(), request.courseId())){
            throw new RuntimeException("Estudante com id " + request.studentId() + " já está inscrito no curso com id " + request.courseId());
        }

        Enrollment enrollment = toEntity(request);

        return toResponseDTO(enrollmentRepository.save(enrollment));
    }

    public Enrollment toEntity(EnrollmentRequestDTO request){
        Student student = studentService.findById(request.studentId());
        Course course = courseService.findById(request.courseId());

        return Enrollment.builder()
            .student(student)
            .course(course)
            .build();
    }

    public EnrollmentResponseDTO toResponseDTO(Enrollment enrollment){
        return new EnrollmentResponseDTO(
            enrollment.getId(),
            studentService.findByIdResponseDTO(enrollment.getStudent().getId()),
            courseService.findByIdResponseDTO(enrollment.getCourse().getId()),
            completedLessonsService.calculateOverallProgress(enrollment),
            enrollment.getStatus(),
            completedLessonsService.findByEnrollment(enrollment)

        );
    }
}
