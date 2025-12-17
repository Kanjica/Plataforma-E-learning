package com.lp3.elearning.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

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

    @Transactional
    public LessonResponseDTO create(LessonRequestDTO lessonRequest, Long moduleId, Long courseId) {
        Module module = moduleService.findById(moduleId);

        if(!module.getCourse().getId().equals(courseId)){
            throw new BusinessRuleException("ID do curso na url difere do id do curso do modulo");
        }

        // Se a ordem já existe, empurra as outras para frente
        if(lessonRepository.existsByLessonOrderAndModuleId(lessonRequest.lessonOrder(), moduleId))
        shiftLessonOrders(moduleId, lessonRequest.lessonOrder(), null);

        Lesson lesson = toEntity(lessonRequest, module);
        return toResponseDTO(lessonRepository.save(lesson));
    }

    public LessonResponseDTO getById(Long lessonId, Long moduleId){
        Lesson lesson = lessonRepository.findByIdAndModuleId(lessonId, moduleId)
            .orElseThrow(() -> new BusinessRuleException("Aula com ID " + lessonId + " não encontrada no módulo " + moduleId)); 
        return toResponseDTO(lesson);
    }   
    
    public void validateLessonAccessibility(Lesson currentLesson, Enrollment enrollment) {
        
        // Validação básica: Curso da matrícula deve bater com o curso da aula
        if (!enrollment.getCourse().getId().equals(currentLesson.getModule().getCourse().getId())) {
            throw new BusinessRuleException("A matrícula não corresponde ao curso desta aula.");
        }

        Integer currentLessonOrder = currentLesson.getLessonOrder();
        
        if (currentLessonOrder > 1) {
            Lesson previousLesson = lessonRepository
                .findByModuleIdAndLessonOrder(currentLesson.getModule().getId(), currentLessonOrder - 1)
                .orElseThrow(() -> new BusinessRuleException("Erro de dados: Aula anterior não encontrada."));
            
            if (!completedLessonsService.isLessonCompleted(enrollment, previousLesson)) {
                throw new BusinessRuleException("Você precisa completar a aula anterior (" + previousLesson.getTitle() + ") antes de acessar esta.");
            }
        }
        
        // CENÁRIO 2: Primeiro aula de um módulo novo (Módulo 2, Aula 1 requer Módulo 1, Última Aula)
        // (Adicionei isso pois seu código original permitia pular do Módulo 1 direto pro Módulo 2 sem terminar o 1)
        else if (currentLessonOrder == 1 && currentLesson.getModule().getModuleOrder() > 1) {
             // Lógica para verificar o final do módulo anterior (opcional, mas recomendado)
             // Você precisaria buscar o módulo anterior e sua última lição.
        }
    }

    // 2. Atualize o método antigo para usar o novo validador
    public LessonResponseDTO getLessonByIdForUser(Long lessonId, Long studentId, Long courseId){
        Enrollment enrollment = enrollmentService.findByStudentIdAndCourseId(studentId, courseId);
        Lesson currentLesson = findById(lessonId);

        // Chama a validação centralizada
        validateLessonAccessibility(currentLesson, enrollment);
        
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

    @Transactional
    public LessonResponseDTO update(Long lessonId, Long moduleId, LessonRequestDTO request) {
        Lesson lesson = lessonRepository.findByIdAndModuleId(lessonId, moduleId)
            .orElseThrow(() -> new BusinessRuleException("Aula não encontrada neste módulo."));

        // Se mudou a ordem, aplica o shift
        if (!lesson.getLessonOrder().equals(request.lessonOrder())) {
            shiftLessonOrders(moduleId, request.lessonOrder(), lessonId);
        }

        lesson.setTitle(request.title());
        lesson.setContent(request.content());
        lesson.setVideoUrl(request.videoUrl());
        lesson.setLessonOrder(request.lessonOrder());

        return toResponseDTO(lessonRepository.save(lesson));
    }

    @Transactional
    public void delete(Long lessonId, Long moduleId) {
        Lesson lessonToDelete = lessonRepository.findByIdAndModuleId(lessonId, moduleId)
            .orElseThrow(() -> new BusinessRuleException("Aula não encontrada para exclusão."));

        Integer removedOrder = lessonToDelete.getLessonOrder();
        lessonRepository.delete(lessonToDelete);

        // Ao deletar, puxamos as próximas para trás para não deixar buracos
        List<Lesson> subsequentLessons = lessonRepository.findByModuleId(moduleId).stream()
            .filter(l -> l.getLessonOrder() > removedOrder)
            .toList();
        
        subsequentLessons.forEach(l -> l.setLessonOrder(l.getLessonOrder() - 1));
        lessonRepository.saveAll(subsequentLessons);
    }

    /**
     * Lógica de deslocamento: se a posição 'newOrder' estiver ocupada, 
     * incrementa ela e todas as subsequentes.
     */
    private void shiftLessonOrders(Long moduleId, Integer newOrder, Long excludedLessonId) {
        List<Lesson> lessons = lessonRepository.findByModuleId(moduleId);
        
        // Filtra aulas que têm ordem maior ou igual à nova ordem
        // Se for um update, excluímos a própria aula da lista de shift
        List<Lesson> toShift = lessons.stream()
            .filter(l -> !l.getId().equals(excludedLessonId))
            .filter(l -> l.getLessonOrder() >= newOrder)
            .sorted(Comparator.comparing(Lesson::getLessonOrder).reversed()) // Ordem reversa para evitar conflito de unique constraint se houver
            .toList();

        if (!toShift.isEmpty()) {
            toShift.forEach(l -> l.setLessonOrder(l.getLessonOrder() + 1));
            lessonRepository.saveAll(toShift);
            lessonRepository.flush(); // Força o update antes de inserir a nova
        }
    }
    public LessonResponseDTO getByLessonOrder(Long moduleId, Integer order, Long studentId, Long courseId) {
        // 1. Busca a aula pela ordem e módulo
        Lesson lesson = lessonRepository.findByModuleIdAndLessonOrder(moduleId, order)
            .orElseThrow(() -> new BusinessRuleException("Aula com ordem " + order + " não encontrada no módulo " + moduleId));

        // 2. Busca a matrícula do aluno
        Enrollment enrollment = enrollmentService.findByStudentIdAndCourseId(studentId, courseId);

        // 3. Reutiliza sua lógica de validação existente!
        validateLessonAccessibility(lesson, enrollment);

        return toResponseDTO(lesson);
    }
}
