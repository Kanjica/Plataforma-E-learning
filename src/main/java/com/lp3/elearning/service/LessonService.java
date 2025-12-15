package com.lp3.elearning.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.LessonReorderRequestDTO;
import com.lp3.elearning.dto.LessonRequestDTO;
import com.lp3.elearning.dto.LessonResponseDTO;
import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.entities.Lesson;
import com.lp3.elearning.repository.LessonRepository;
import com.lp3.elearning.entities.Module;
import com.lp3.elearning.exception.BusinessRuleException;

import jakarta.transaction.Transactional;

@Service
public class LessonService {
    
    private final LessonRepository lessonRepository;
    private final ModuleService moduleService;
    private final CompletedLessonsService completedLessonsService;
    private final EnrollmentService enrollmentService;


    public LessonService(LessonRepository lessonRepository, 
            ModuleService moduleService, 
            @Lazy CompletedLessonsService completedLessonsService,
            @Lazy EnrollmentService enrollmentService) {
        this.lessonRepository = lessonRepository;
        this.moduleService = moduleService;
        this.completedLessonsService = completedLessonsService;
        this.enrollmentService = enrollmentService;
    }

    public LessonResponseDTO create(LessonRequestDTO lessonRequest) {
        Long moduleId = lessonRequest.moduleId();
        
        Module module = moduleService.findById(moduleId);
        
        Lesson lesson = toEntity(lessonRequest, module);
        
        return toResponseDTO(lessonRepository.save(lesson));
    }

    public LessonResponseDTO getById(Long lessonId, Long moduleId){
        Lesson lesson = lessonRepository.findByIdAndModuleId(lessonId, moduleId)
            .orElseThrow(() -> new BusinessRuleException("Aula com ID " + lessonId + " não encontrada no módulo " + moduleId)); 
        return toResponseDTO(lesson);
    }   
    
    public LessonResponseDTO getLessonByIdForUser(Long lessonId, Long studentId, Long courseId){
    
        // 1. OBTEM A MATRÍCULA (Enrollment) pelo ID do Estudante e ID do Curso
        Enrollment enrollment = enrollmentService.findByStudentIdAndCourseId(studentId, courseId);
        
        // 2. Continua a lógica de validação
        
        Lesson currentLesson = findById(lessonId);
        
        // Validação do Curso (redundante se o findByStudentIdAndCourseId já for feito, mas bom para garantir)
        if (!enrollment.getCourse().getId().equals(currentLesson.getModule().getCourse().getId())) {
            throw new BusinessRuleException("Erro de dados. A matrícula não corresponde ao curso desta aula.");
        }
            
        // 3. Verificar a Ordem e Progresso (Se a aula anterior foi concluída)
        
        // Aulas são ordenadas por lessonOrder dentro do Módulo.
        Integer currentLessonOrder = currentLesson.getLessonOrder();
        
        if (currentLessonOrder > 1) {
            // Não é a primeira aula do módulo, precisamos verificar a anterior.
            
            // 3.1. Encontrar a aula anterior no mesmo módulo
            Lesson previousLesson = lessonRepository
                .findByModuleIdAndLessonOrder(currentLesson.getModule().getId(), currentLessonOrder - 1)
                .orElse(null); // Caso haja falha na ordenação (não deve ocorrer)
            
            if (previousLesson == null) {
                // Isso pode acontecer se houver um erro de dados onde a ordem não é sequencial (e.g., 1, 3, 4).
                throw new BusinessRuleException("Erro na ordem da aula. Aula anterior não encontrada.");
            }
            
            // 3.2. Verificar se a aula anterior foi concluída
            if (!completedLessonsService.isLessonCompleted(enrollment, previousLesson)) {
                throw new BusinessRuleException("Acesso negado. A aula anterior (Ordem " + (currentLessonOrder - 1) + ") precisa ser concluída primeiro.");
            }
        }
        
        // Se todas as validações passarem
        return toResponseDTO(currentLesson);
    }
    
    public List<LessonResponseDTO> getAllByModuleId(Long moduleId) {
        List<Lesson> lessons = lessonRepository.findByModuleId(moduleId);
        return lessons.stream()
                .sorted(Comparator.comparing(Lesson::getLessonOrder))
                .map(this::toResponseDTO)
                .toList();
    }
    
    
    @Transactional
    public List<LessonResponseDTO> reorder(Long moduleId, List<LessonReorderRequestDTO> requests) {
        
        if(!moduleService.existsById(moduleId)){
            throw new BusinessRuleException("Módulo com ID " + moduleId + " não existe.");
        }

        List<Lesson> currentLessons = lessonRepository.findByModuleId(moduleId);
        
        Map<Long, Lesson> lessonMap = currentLessons.stream()
            .collect(Collectors.toMap(Lesson::getId, lesson -> lesson));
        
        if(requests.size() != currentLessons.size() || 
            requests.stream().anyMatch(r -> !lessonMap.containsKey(r.lessonId()))){
            throw new BusinessRuleException("A lista de IDs para reordenação está incompleta ou contém IDs inválidos para o módulo " + moduleId);
        }
        
        requests.forEach(request -> {
            Lesson lessonToUpdate = lessonMap.get(request.lessonId());
            lessonToUpdate.setLessonOrder(request.newOrder()); 
        });
        
        List<Lesson> updatedLessons = lessonRepository.saveAll(currentLessons);
        
        return updatedLessons.stream()
            .sorted(Comparator.comparing(Lesson::getLessonOrder))
            .map(this::toResponseDTO)
            .toList();
    }

    public LessonResponseDTO toResponseDTO(Lesson lesson){
        return new LessonResponseDTO(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getContent(),
                lesson.getLessonOrder(),
                lesson.getVideoUrl(),
                lesson.getModule().getId(),
                lesson.getModule().getTitle(),
                lesson.getModule().getCourse().getId(),
                lesson.getModule().getCourse().getTitle()
        );
    }
    
    public Lesson toEntity(LessonRequestDTO request, Module module) {
        return Lesson.builder()
                .title(request.title())
                .content(request.content())
                .lessonOrder(request.lessonOrder())
                .videoUrl(request.videoUrl())
                .module(module)
                .build();
    }

    public Integer countLessonsInModule(Long moduleId) {
        return lessonRepository.findByModuleId(moduleId).size();
    }

    public Integer countLessonsInCourse(Long courseId) {
        List<Object[]> results = lessonRepository.countLessonsPerModuleInCourse(courseId);
        return results.stream()
            .mapToInt(obj -> ((Long) obj[1]).intValue())
            .sum();
    }

    public Lesson findById(Long lessonId) {
        return lessonRepository.findById(lessonId)
            .orElseThrow(() -> new BusinessRuleException("Aula com ID " + lessonId + " não encontrada."));
    }
}
