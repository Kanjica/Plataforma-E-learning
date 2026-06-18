package com.lp3.elearning.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.course.LessonReorderRequestDTO;
import com.lp3.elearning.dto.course.LessonRequestDTO;
import com.lp3.elearning.dto.course.LessonResponseDTO;
import com.lp3.elearning.entities.Enrollment;
import com.lp3.elearning.entities.Lesson;
import com.lp3.elearning.entities.Module;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.mapper.LessonMapper;
import com.lp3.elearning.repository.LessonRepository;
import com.lp3.elearning.security.anottation.Auditable;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "lessons")
public class LessonService {
    
    private final LessonRepository lessonRepository;
    private final ModuleService moduleService;
    private final EnrollmentService enrollmentService;
    private final LessonMapper lessonMapper;
    private final ProgressService progressService; 
    private final CacheManager cacheManager; 

    /**
     * Cria uma nova aula e ajusta a ordenação se necessário.
     * @throws BusinessRuleException se o módulo não pertencer ao curso informado.
     */
    @Transactional
    @Auditable(action = "CRIAR_AULA")
    public LessonResponseDTO create(LessonRequestDTO lessonRequest, Long moduleId) {
        Module module = moduleService.findById(moduleId);

        Long courseId = module.getCourse().getId();

        if(!module.getCourse().getId().equals(courseId)){
            throw new BusinessRuleException("Conflito: O módulo informado não pertence ao curso da URL.");
        }

        // Se a ordem já existe, empurra as outras para frente para abrir espaço
        if(lessonRepository.existsByLessonOrderAndModuleId(lessonRequest.lessonOrder(), moduleId)) {
            shiftLessonOrders(moduleId, lessonRequest.lessonOrder(), null);
        }

        Lesson lesson = lessonMapper.toEntity(lessonRequest);
        lesson.setModule(module);
        
        return lessonMapper.toResponseDTO(lessonRepository.save(lesson));
    }

    public Lesson findByModuleAndOrder(Long moduleId, Integer lessonOrder) {
        return lessonRepository.findByModuleIdAndLessonOrder(moduleId, lessonOrder)
            .orElseThrow(() -> new BusinessRuleException("Aula número " + lessonOrder + " não existe neste módulo."));
    }
    /**
     * Busca uma aula para consumo do aluno, validando regras de acesso e sequência.
     * Garante que o aluno não pule aulas ou módulos.
     */
    @Transactional(readOnly = true)
    public LessonResponseDTO getLessonByIdForUser(Long lessonId, Long studentId){
        Long courseId = lessonRepository.findCourseIdById(lessonId)
            .orElseThrow(() -> new BusinessRuleException("Aula não encontrada com ID: " + lessonId));

        Enrollment enrollment = enrollmentService.findByStudentIdAndCourseId(studentId, courseId);
        Lesson currentLesson = findById(lessonId);

        Lesson previous = null;
        if (currentLesson.getLessonOrder() > 1) {
            previous = findByModuleAndOrder(currentLesson.getModule().getId(), currentLesson.getLessonOrder() - 1);
        }

        progressService.validateLessonAccessibility(currentLesson, enrollment, previous);
        
        return lessonMapper.toResponseDTO(currentLesson);
    }

    @Transactional
    @Auditable(action = "REORDENAR_AULAS")
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
        
        currentLessons.forEach(l -> cacheManager.getCache("lessons").evict(l.getId()));
        Module module = moduleService.findById(moduleId);
        cacheManager.getCache("courses").evict(module.getCourse().getId());

        return lessonRepository.saveAll(currentLessons).stream()
            .sorted(Comparator.comparing(Lesson::getLessonOrder))
            .map(lessonMapper::toResponseDTO)
            .toList();
    }

    // --- Métodos Auxiliares e CRUD Simples ---

    public Lesson findById(Long lessonId) {
        return lessonRepository.findById(lessonId)
            .orElseThrow(() -> new BusinessRuleException("Aula não encontrada com ID: " + lessonId));
    }

    @Transactional
    @Auditable(action = "DELETAR_AULA")
    public void delete(Long lessonId) {
        Lesson lessonToDelete = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new BusinessRuleException("Aula não encontrada."));

        Integer removedOrder = lessonToDelete.getLessonOrder();
        lessonRepository.delete(lessonToDelete);

        Long moduleId = lessonToDelete.getModule().getId();
        
        // Reorganiza para não deixar "buracos" na numeração (ex: 1, 3, 4 vira 1, 2, 3)
        List<Lesson> remainingLessons = lessonRepository.findByModuleId(moduleId);
        remainingLessons.stream()
            .filter(l -> l.getLessonOrder() > removedOrder)
            .forEach(l -> l.setLessonOrder(l.getLessonOrder() - 1));
        
        lessonRepository.saveAll(remainingLessons);

        cacheManager.getCache("lessons").evict(lessonId);
        Module module = moduleService.findById(moduleId);
        cacheManager.getCache("modules").evict(moduleId);
        Course course = module.getCourse();
        cacheManager.getCache("courses").evict(course.getId());
    }

    private void shiftLessonOrders(Long moduleId, Integer startOrder, Long ignoreLessonId) {
        List<Lesson> lessons = lessonRepository.findByModuleId(moduleId);
        lessons.stream()
            .filter(l -> !l.getId().equals(ignoreLessonId))
            .filter(l -> l.getLessonOrder() >= startOrder)
            .forEach(l -> l.setLessonOrder(l.getLessonOrder() + 1));
        
        lessonRepository.saveAll(lessons);

        lessons.forEach(l -> cacheManager.getCache("lessons").evict(l.getId()));
        Module module = moduleService.findById(moduleId);
        cacheManager.getCache("modules").evict(moduleId);
        Course course = module.getCourse();
        cacheManager.getCache("courses").evict(course.getId());
    }

    @Cacheable(key = "#moduleId")
    public List<LessonResponseDTO> getAllByModuleId(Long moduleId) {
        return lessonRepository.findByModuleId(moduleId).stream()
            .sorted(Comparator.comparing(Lesson::getLessonOrder))
            .map(lessonMapper::toResponseDTO).toList();
    }

    public Integer countLessonsInCourse(Long courseId) {
        return lessonRepository.countLessonsPerModuleInCourse(courseId).stream()
            .mapToInt(obj -> ((Long) obj[1]).intValue()).sum();
    }
    
    public LessonResponseDTO getByLessonOrder(Integer order, Long moduleId, Long studentId) {
        Lesson lesson = lessonRepository.findByModuleIdAndLessonOrder(moduleId, order)
            .orElseThrow(() -> new BusinessRuleException("Aula número " + order + " não existe neste módulo."));
        
        Long courseId = lesson.getModule().getCourse().getId();
        Enrollment enrollment = enrollmentService.findByStudentIdAndCourseId(studentId, courseId);

        // 1. Busca a aula anterior se não for a primeira
        Lesson previous = null;
        if (order > 1) {
            previous = lessonRepository.findByModuleIdAndLessonOrder(moduleId, order - 1)
                .orElse(null); // Se não achar a anterior, o validador cuida do erro
        }

        // 2. Valida usando o ProgressService (sem dependência circular)
        progressService.validateLessonAccessibility(lesson, enrollment, previous);
        
        return lessonMapper.toResponseDTO(lesson);
    }
        
    @Transactional
    @Auditable(action = "ATUALIZAR_AULA")
    public LessonResponseDTO update(Long lessonId, LessonRequestDTO request) {

        Lesson lesson = findById(lessonId);

        Long moduleId = lesson.getModule().getId();
        
        if (!lesson.getLessonOrder().equals(request.lessonOrder())) {
            shiftLessonOrders(moduleId, request.lessonOrder(), lessonId);
        }
        lesson.setTitle(request.title());
        lesson.setContent(request.content());
        lesson.setVideoUrl(request.videoUrl());
        lesson.setLessonOrder(request.lessonOrder());

        cacheManager.getCache("lessons").evict(lessonId);
        Module module = moduleService.findById(moduleId);
        cacheManager.getCache("modules").evict(moduleId);
        Course course = module.getCourse();
        cacheManager.getCache("courses").evict(course.getId());
        
        return lessonMapper.toResponseDTO(lessonRepository.save(lesson));
    }
}