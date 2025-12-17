package com.lp3.elearning.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.CompletedLessonResponseDTO;
import com.lp3.elearning.entities.CompletedLesson;
import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.entities.Lesson;
import com.lp3.elearning.entities.StatusEnrollment;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.repository.CompletedLessonRepository;

import jakarta.transaction.Transactional;

@Service 
public class CompletedLessonsService {

    private final CompletedLessonRepository completedLessonRepository;
    private final EnrollmentService enrollmentService;
    private final LessonService lessonService;

    public CompletedLessonsService(
            CompletedLessonRepository completedLessonRepository, 
            LessonService lessonService, 
            @Lazy EnrollmentService enrollmentService) {
        this.completedLessonRepository = completedLessonRepository;
        this.lessonService = lessonService;
        this.enrollmentService = enrollmentService;
    }

    @Transactional
    public CompletedLessonResponseDTO completeLesson(Long studentId, Long courseId,Long lessonId){
        Enrollment existingEnrollment = enrollmentService.findByStudentIdAndCourseId(
            studentId, 
            courseId
        );

        Lesson lesson = lessonService.findById(lessonId);

        // --- ADICIONE ESTA LINHA AQUI ---
        // Isso impede que alguém complete a aula 3 se a 2 não estiver pronta,
        // mesmo que tente burlar via Postman/API.
        lessonService.validateLessonAccessibility(lesson, existingEnrollment);
        // --------------------------------

        // Verifica se JÁ não foi completada antes para evitar duplicidade/erro
        if(isLessonCompleted(existingEnrollment, lesson)){
             throw new BusinessRuleException("Esta aula já foi concluída.");
        }

        CompletedLesson completedLesson = CompletedLesson.builder()
            .enrollment(existingEnrollment) 
            .lesson(lesson)
            .completionDate(LocalDateTime.now())
            .build();
        
        // 2. Salva a lição concluída PRIMEIRO
        // Se o seu cálculo de progresso faz um count() no banco, 
        // essa linha precisa vir antes do cálculo.
        CompletedLesson savedLesson = completedLessonRepository.save(completedLesson);

        // 3. Calcula o novo progresso
        // (Assumindo que calculateOverallProgress lê do banco ou da lista atualizada)
        double newProgress = enrollmentService.calculateOverallProgress(existingEnrollment);
        
        // 4. ATUALIZA A ENTIDADE (O passo que faltava)
        existingEnrollment.setOverallProgress(newProgress);
        
        if (newProgress >= 1.0) {
            existingEnrollment.setStatus(StatusEnrollment
                .COMPLETED); // Ajuste conforme seu Enum
        }

        System.out.println("Salvando matricula com novo progresso: " + newProgress);
        
        // 5. Persiste a mudança da matrícula no banco
        enrollmentService.saveProgress(existingEnrollment);

        // Retorna o DTO
        return toResponseDTO(savedLesson);
    }

    public boolean isLessonCompleted(Enrollment enrollment, Lesson lesson) {
        // Delega para o Repositório verificar a existência do registro.
        // É necessário ter o método existsByEnrollmentAndLesson no CompletedLessonRepository.
        return completedLessonRepository.existsByEnrollmentAndLesson(enrollment, lesson);
    }

    public Integer countByEnrollment(Enrollment enrollment) {
        return completedLessonRepository.countByEnrollment(enrollment);
    }

    public Set<CompletedLessonResponseDTO> findByEnrollment(Enrollment enrollment) {
        return toResponseDTOs(completedLessonRepository.findByEnrollment(enrollment));
    }

    public Set<CompletedLessonResponseDTO> toResponseDTOs(List<CompletedLesson> completedLessons){
        return completedLessons.stream().map(this::toResponseDTO).collect(Collectors.toSet());
    }

    public CompletedLessonResponseDTO toResponseDTO(CompletedLesson completedLesson){
        return new CompletedLessonResponseDTO(
            completedLesson.getId(),
            lessonService.toResponseDTO(completedLesson.getLesson()),
            completedLesson.getCompletionDate().toString(),
            enrollmentService.calculateOverallProgress(completedLesson.getEnrollment())
        );
    }
}