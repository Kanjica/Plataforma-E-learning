package com.lp3.elearning.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante: use o do Spring

import com.lp3.elearning.dto.course.LessonReorderRequestDTO;
import com.lp3.elearning.dto.course.LessonRequestDTO;
import com.lp3.elearning.dto.course.LessonResponseDTO;
import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.entities.Lesson;
import com.lp3.elearning.entities.Module;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.repository.LessonRepository;

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

    /**
     * Cria uma nova aula e ajusta a ordenação se necessário.
     * @throws BusinessRuleException se o módulo não pertencer ao curso informado.
     */
    @Transactional
    public LessonResponseDTO create(LessonRequestDTO lessonRequest, Long moduleId, Long courseId) {
        Module module = moduleService.findById(moduleId);

        if(!module.getCourse().getId().equals(courseId)){
            throw new BusinessRuleException("Conflito: O módulo informado não pertence ao curso da URL.");
        }

        // Se a ordem já existe, empurra as outras para frente para abrir espaço
        if(lessonRepository.existsByLessonOrderAndModuleId(lessonRequest.lessonOrder(), moduleId)) {
            shiftLessonOrders(moduleId, lessonRequest.lessonOrder(), null);
        }

        Lesson lesson = toEntity(lessonRequest, module);
        return toResponseDTO(lessonRepository.save(lesson));
    }

    /**
     * Busca uma aula para consumo do aluno, validando regras de acesso e sequência.
     * Garante que o aluno não pule aulas ou módulos.
     */
    @Transactional(readOnly = true)
    public LessonResponseDTO getLessonByIdForUser(Long lessonId, Long studentId, Long courseId){
        Enrollment enrollment = enrollmentService.findByStudentIdAndCourseId(studentId, courseId);
        Lesson currentLesson = findById(lessonId);

        validateLessonAccessibility(currentLesson, enrollment);
        
        return toResponseDTO(currentLesson);
    }

    /**
     * Valida se o aluno pode assistir a aula atual baseada no seu progresso.
     * Regra 1: Deve estar matriculado.
     * Regra 2: Se não for a primeira aula, a anterior deve estar concluída.
     * Regra 3: Se for a primeira aula de um módulo novo, o módulo anterior deve estar finalizado.
     */
    public void validateLessonAccessibility(Lesson currentLesson, Enrollment enrollment) {
        if (!enrollment.getCourse().getId().equals(currentLesson.getModule().getCourse().getId())) {
            throw new BusinessRuleException("Acesso negado: Esta aula não pertence ao curso matriculado.");
        }

        Integer currentOrder = currentLesson.getLessonOrder();
        Long moduleId = currentLesson.getModule().getId();

        // CENÁRIO A: Sequência dentro do mesmo módulo
        if (currentOrder > 1) {
            Lesson previous = lessonRepository.findByModuleIdAndLessonOrder(moduleId, currentOrder - 1)
                .orElseThrow(() -> new BusinessRuleException("Erro de integridade: Aula anterior não encontrada."));
            
            if (!completedLessonsService.isLessonCompleted(enrollment, previous)) {
                throw new BusinessRuleException("Bloqueado: Você precisa concluir a aula '" + previous.getTitle() + "' antes de avançar.");
            }
        } 
        // CENÁRIO B: Transição entre módulos (Primeira aula do módulo X exige fim do módulo X-1)
        else if (currentOrder == 1 && currentLesson.getModule().getModuleOrder() > 1) {
            Module previousModule = moduleService.findByCourseIdAndModuleOrder(
                currentLesson.getModule().getCourse().getId(), 
                currentLesson.getModule().getModuleOrder() - 1
            );

            // Busca a última aula do módulo anterior
            lessonRepository.findFirstByModuleIdOrderByLessonOrderDesc(previousModule.getId())
                .ifPresent(lastLessonOfPrevModule -> {
                    if (!completedLessonsService.isLessonCompleted(enrollment, lastLessonOfPrevModule)) {
                        throw new BusinessRuleException("Bloqueado: Complete o módulo '" + previousModule.getTitle() + "' antes de iniciar este.");
                    }
                });
        }
    }

    @Transactional
    public List<LessonResponseDTO> reorder(Long moduleId, List<LessonReorderRequestDTO> requests) {
        if(!moduleService.existsById(moduleId)){
            throw new BusinessRuleException("Módulo não encontrado.");
        }

        List<Lesson> currentLessons = lessonRepository.findByModuleId(moduleId);
        Map<Long, Lesson> lessonMap = currentLessons.stream().collect(Collectors.toMap(Lesson::getId, l -> l));
        
        // Valida se todos os IDs enviados pertencem ao módulo
        if(requests.stream().anyMatch(r -> !lessonMap.containsKey(r.lessonId()))){
            throw new BusinessRuleException("Tentativa de reordenar aulas que não pertencem a este módulo.");
        }
        
        requests.forEach(req -> {
            Lesson lesson = lessonMap.get(req.lessonId());
            if (lesson != null) lesson.setLessonOrder(req.newOrder());
        });
        
        return lessonRepository.saveAll(currentLessons).stream()
            .sorted(Comparator.comparing(Lesson::getLessonOrder))
            .map(this::toResponseDTO)
            .toList();
    }

    // --- Métodos Auxiliares e CRUD Simples ---

    public Lesson findById(Long lessonId) {
        return lessonRepository.findById(lessonId)
            .orElseThrow(() -> new BusinessRuleException("Aula não encontrada com ID: " + lessonId));
    }

    @Transactional
    public void delete(Long lessonId, Long moduleId) {
        Lesson lessonToDelete = lessonRepository.findByIdAndModuleId(lessonId, moduleId)
            .orElseThrow(() -> new BusinessRuleException("Aula não encontrada."));

        Integer removedOrder = lessonToDelete.getLessonOrder();
        lessonRepository.delete(lessonToDelete);

        // Reorganiza para não deixar "buracos" na numeração (ex: 1, 3, 4 vira 1, 2, 3)
        List<Lesson> remainingLessons = lessonRepository.findByModuleId(moduleId);
        remainingLessons.stream()
            .filter(l -> l.getLessonOrder() > removedOrder)
            .forEach(l -> l.setLessonOrder(l.getLessonOrder() - 1));
        
        lessonRepository.saveAll(remainingLessons);
    }

    private void shiftLessonOrders(Long moduleId, Integer startOrder, Long ignoreLessonId) {
        List<Lesson> lessons = lessonRepository.findByModuleId(moduleId);
        lessons.stream()
            .filter(l -> !l.getId().equals(ignoreLessonId))
            .filter(l -> l.getLessonOrder() >= startOrder)
            .forEach(l -> l.setLessonOrder(l.getLessonOrder() + 1));
        
        lessonRepository.saveAll(lessons);
    }

    // Métodos de conversão e leitura simples mantidos...
    public LessonResponseDTO toResponseDTO(Lesson lesson){
        return new LessonResponseDTO(
            lesson.getId(), lesson.getTitle(), lesson.getContent(),
            lesson.getLessonOrder(), lesson.getVideoUrl(),
            lesson.getModule().getId(), lesson.getModule().getTitle(),
            lesson.getModule().getCourse().getId(), lesson.getModule().getCourse().getTitle()
        );
    }

    public Lesson toEntity(LessonRequestDTO request, Module module) {
        return Lesson.builder()
            .title(request.title()).content(request.content())
            .lessonOrder(request.lessonOrder()).videoUrl(request.videoUrl())
            .module(module).build();
    }

    public List<LessonResponseDTO> getAllByModuleId(Long moduleId) {
        return lessonRepository.findByModuleId(moduleId).stream()
            .sorted(Comparator.comparing(Lesson::getLessonOrder))
            .map(this::toResponseDTO).toList();
    }

    public Integer countLessonsInCourse(Long courseId) {
        return lessonRepository.countLessonsPerModuleInCourse(courseId).stream()
            .mapToInt(obj -> ((Long) obj[1]).intValue()).sum();
    }
    
    public LessonResponseDTO getByLessonOrder(Long moduleId, Integer order, Long studentId, Long courseId) {
        Lesson lesson = lessonRepository.findByModuleIdAndLessonOrder(moduleId, order)
            .orElseThrow(() -> new BusinessRuleException("Aula número " + order + " não existe neste módulo."));
        
        Enrollment enrollment = enrollmentService.findByStudentIdAndCourseId(studentId, courseId);
        validateLessonAccessibility(lesson, enrollment);
        
        return toResponseDTO(lesson);
    }
    
    @Transactional
    public LessonResponseDTO update(Long lessonId, Long moduleId, LessonRequestDTO request) {
        Lesson lesson = findById(lessonId);
        if (!lesson.getLessonOrder().equals(request.lessonOrder())) {
            shiftLessonOrders(moduleId, request.lessonOrder(), lessonId);
        }
        lesson.setTitle(request.title());
        lesson.setContent(request.content());
        lesson.setVideoUrl(request.videoUrl());
        lesson.setLessonOrder(request.lessonOrder());
        return toResponseDTO(lessonRepository.save(lesson));
    }
}