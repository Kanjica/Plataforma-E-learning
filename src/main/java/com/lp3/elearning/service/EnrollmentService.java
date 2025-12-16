package com.lp3.elearning.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.EnrollmentRequestDTO;
import com.lp3.elearning.dto.EnrollmentResponseDTO;
import com.lp3.elearning.entities.Course;
import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.repository.EnrollmentRepository;
import java.util.List;
import java.util.stream.Collectors;


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

    public Enrollment saveProgress(Enrollment enrollment){
        return enrollmentRepository.save(enrollment);
    }
    public Enrollment findById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Matrícula não encontrada com o ID: " + id));
    }

    public Double calculateOverallProgress(Enrollment enrollment){
        Integer totalLessons = lessonService.countLessonsInCourse(enrollment.getCourse().getId());
        Integer completedLessons = completedLessonsService.countByEnrollment(enrollment);

        if (totalLessons == 0){
            return 0.0;
        }

        return (double) completedLessons / totalLessons;
    }

    public Enrollment findByStudentIdAndCourseId(Long studentId, Long courseId) {
        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
            .orElseThrow(() -> new RuntimeException("Estudante não está matriculado neste curso."));
    }

    public List<EnrollmentResponseDTO> getMyEnrollments(Long studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentIdOrderByEnrollmentDateDesc(studentId);
        
        return enrollments.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
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
            courseService.getCourseByIdResponseDTO(enrollment.getCourse().getId()),
            calculateOverallProgress(enrollment),
            enrollment.getStatus(),
            completedLessonsService.findByEnrollment(enrollment)

        );
    }
}
