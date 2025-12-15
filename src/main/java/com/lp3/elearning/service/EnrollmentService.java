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
    private final LessonService lessonService;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, StudentService studentService, CourseService courseService, CompletedLessonsService completedLessonsService, LessonService lessonService) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentService = studentService;
        this.courseService = courseService;
        this.completedLessonsService = completedLessonsService;
        this.lessonService = lessonService;
    }

    public EnrollmentResponseDTO create(EnrollmentRequestDTO request){
        if(enrollmentRepository.existsByStudentIdAndCourseId(request.studentId(), request.courseId())){
            throw new RuntimeException("Estudante com id " + request.studentId() + " já está inscrito no curso com id " + request.courseId());
        }

        Enrollment enrollment = toEntity(request);

        return toResponseDTO(enrollmentRepository.save(enrollment));
    }


    public Double calculateOverallProgress(Enrollment enrollment){
        Integer totalLessons = lessonService.countLessonsInCourse(enrollment.getCourse().getId());
        Integer completedLessons = completedLessonsService.countByEnrollment(enrollment);

        if (totalLessons == 0){
            return 0.0;
        }

        return (double) completedLessons / totalLessons;
    }

    public Enrollment findById(Long enrollmentId){
        return enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new RuntimeException("Matrícula com ID " + enrollmentId + " não encontrada."));
    }


    public Enrollment findByStudentIdAndCourseId(Long studentId, Long courseId) {
        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
            .orElseThrow(() -> new RuntimeException("Estudante não está matriculado neste curso."));
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
            calculateOverallProgress(enrollment),
            enrollment.getStatus(),
            completedLessonsService.findByEnrollment(enrollment)

        );
    }
}
