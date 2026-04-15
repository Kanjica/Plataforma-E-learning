package com.lp3.elearning.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.enrollment.CompletedLessonResponseDTO;
import com.lp3.elearning.dto.enrollment.EnrollmentRequestDTO;
import com.lp3.elearning.dto.enrollment.EnrollmentResponseDTO;
import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.entities.StatusEnrollment;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.exception.ConflictException;
import com.lp3.elearning.mapper.EnrollmentMapper;
import com.lp3.elearning.repository.EnrollmentRepository;
import com.lp3.elearning.security.anottation.Auditable;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final CompletedLessonsService completedLessonsService;  
    private final EnrollmentMapper enrollmentMapper;

    @Transactional
    @Auditable(action = "CRIAR_MATRICULA")
    public EnrollmentResponseDTO create(EnrollmentRequestDTO request){
        if(enrollmentRepository.existsByStudentIdAndCourseId(request.studentId(), request.courseId())){
            throw new ConflictException("O aluno já possui matrícula ativa neste curso.");
        }

        Enrollment enrollment = enrollmentMapper.toEntity(request);
        enrollment.setStatus(StatusEnrollment.IN_PROGRESS); // Define status inicial explícito
        
        return enrollmentMapper.toResponseDTO(enrollmentRepository.save(enrollment));
    }

    /**
     * Calcula o progresso (0 a 1) do aluno baseado nas aulas concluídas vs total.
     */
    public Double calculateOverallProgress(Enrollment enrollment){
        return enrollmentRepository.getProgress(enrollment.getId(), enrollment.getCourse().getId());
    }
    
    @Transactional(readOnly = true)
    public Enrollment findByStudentIdAndCourseId(Long studentId, Long courseId) {
        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
            .orElseThrow(() -> new BusinessRuleException("Matrícula não encontrada. O aluno não tem acesso a este curso."));
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> getMyEnrollments(Long studentId) {
        return enrollmentRepository.findByStudentIdOrderByEnrollmentDateDesc(studentId).stream()
                .map(enrollmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // --- Métodos de Conversão e Auxiliares ---
    
    public Enrollment findById(Long id) {
        return enrollmentRepository.findById(id).orElseThrow(() -> new BusinessRuleException("Matrícula não encontrada."));
    }
    
    public Page<EnrollmentResponseDTO> findByStudent(Long studentId, Pageable pageable) {
        return enrollmentRepository.findByStudentId(studentId, pageable)
                .map(enrollmentMapper::toResponseDTO);
    }
    
    public Set<CompletedLessonResponseDTO> calculateProgress(Enrollment enrollment){
        return completedLessonsService.findByEnrollment(enrollment);
    }

    public Enrollment saveProgress(Enrollment enrollment){
        return enrollmentRepository.save(enrollment);
    }
}