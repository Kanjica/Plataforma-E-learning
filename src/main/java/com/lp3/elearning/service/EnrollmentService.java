package com.lp3.elearning.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.CompletedLessonResponseDTO;
import com.lp3.elearning.dto.EnrollmentRequestDTO;
import com.lp3.elearning.dto.EnrollmentResponseDTO;
import com.lp3.elearning.entities.Course;
import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.entities.StatusEnrollment;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.exception.ConflictException;
import com.lp3.elearning.repository.EnrollmentRepository;

@Service
public class EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final StudentService studentService;
    private final CourseService courseService;
    private final CompletedLessonsService completedLessonsService;
    private final LessonService lessonService;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, 
                             StudentService studentService, 
                             CourseService courseService, 
                             @Lazy CompletedLessonsService completedLessonsService, // Lazy para evitar ciclo
                             @Lazy LessonService lessonService) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentService = studentService;
        this.courseService = courseService;
        this.completedLessonsService = completedLessonsService;
        this.lessonService = lessonService;
    }

    @Transactional
    public EnrollmentResponseDTO create(EnrollmentRequestDTO request){
        if(enrollmentRepository.existsByStudentIdAndCourseId(request.studentId(), request.courseId())){
            throw new ConflictException("O aluno já possui matrícula ativa neste curso.");
        }

        Enrollment enrollment = toEntity(request);
        enrollment.setStatus(StatusEnrollment.IN_PROGRESS); // Define status inicial explícito
        
        return toResponseDTO(enrollmentRepository.save(enrollment));
    }

    /**
     * Calcula o progresso (0 a 1) do aluno baseado nas aulas concluídas vs total.
     */
    public Double calculateOverallProgress(Enrollment enrollment){
        Integer totalLessons = lessonService.countLessonsInCourse(enrollment.getCourse().getId());
        
        if (totalLessons == 0) return 1.0; // Curso sem aulas é automaticamente "completo" ou 0, depende da regra. 1.0 evita divisão por zero.

        Integer completedCount = completedLessonsService.countByEnrollment(enrollment);
        
        double progress = (double) completedCount / totalLessons;
        
        // Arredonda para 2 casas decimais e garante teto de 100%
        return Math.min(Math.round(progress * 100.0) / 100.0, 1.0);
    }
    
    @Transactional(readOnly = true)
    public Enrollment findByStudentIdAndCourseId(Long studentId, Long courseId) {
        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
            .orElseThrow(() -> new BusinessRuleException("Matrícula não encontrada. O aluno não tem acesso a este curso."));
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> getMyEnrollments(Long studentId) {
        return enrollmentRepository.findByStudentIdOrderByEnrollmentDateDesc(studentId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // --- Métodos de Conversão e Auxiliares ---

    public Enrollment toEntity(EnrollmentRequestDTO request){
        Student student = studentService.findById(request.studentId());
        Course course = courseService.findById(request.courseId());
        return Enrollment.builder().student(student).course(course).build();
    }

    public EnrollmentResponseDTO toResponseDTO(Enrollment enrollment){
        return new EnrollmentResponseDTO(
            enrollment.getId(),
            studentService.findByIdResponseDTO(enrollment.getStudent().getId()),
            courseService.getCourseByIdResponseDTO(enrollment.getCourse().getId()).id(),
            courseService.getCourseByIdResponseDTO(enrollment.getCourse().getId()).title(),
            calculateOverallProgress(enrollment),
            enrollment.getStatus(),
            completedLessonsService.findByEnrollment(enrollment)
        );
    }
    
    // Métodos extras mantidos...
    public Enrollment findById(Long id) {
        return enrollmentRepository.findById(id).orElseThrow(() -> new BusinessRuleException("Matrícula não encontrada."));
    }
    
    public List<EnrollmentResponseDTO> findByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream().map(this::toResponseDTO).toList();
    }
    
    public Set<CompletedLessonResponseDTO> calculateProgress(Enrollment enrollment){
        return completedLessonsService.findByEnrollment(enrollment);
    }

    public Enrollment saveProgress(Enrollment enrollment){
        return enrollmentRepository.save(enrollment);
    }
}