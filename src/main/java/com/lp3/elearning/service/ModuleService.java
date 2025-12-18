package com.lp3.elearning.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.ModuleLessonCountDTO;
import com.lp3.elearning.dto.ModuleReorderRequestDTO;
import com.lp3.elearning.dto.ModuleRequestDTO;
import com.lp3.elearning.dto.ModuleResponseDTO;
import com.lp3.elearning.entities.Course;
import com.lp3.elearning.entities.Module;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.exception.ConflictException;
import com.lp3.elearning.exception.ResourceNotFoundException;
import com.lp3.elearning.repository.CourseRepository;
import com.lp3.elearning.repository.LessonRepository;
import com.lp3.elearning.repository.ModuleRepository;

@Service
public class ModuleService {

    private final CourseService courseService;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonService lessonService;

    public ModuleService(
        @Lazy CourseService courseService, 
        ModuleRepository moduleRepository, 
        LessonRepository lessonRepository, 
        CourseRepository courseRepository, 
        @Lazy LessonService lessonService) {
        this.courseService = courseService;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.lessonService = lessonService;
    }
    
    @Transactional
    public ModuleResponseDTO create(ModuleRequestDTO request, Long courseId){
        Course course = courseService.findById(courseId);

        // Valida duplicidade de título no mesmo curso
        if(moduleRepository.findByTitleAndCourseId(request.title(), courseId).isPresent()){
            throw new ConflictException("Módulo com o título '" + request.title() + "' já existe neste curso.");
        }

        // Valida duplicidade de ordem
        if(moduleRepository.findByModuleOrderAndCourseId(request.moduleOrder(), courseId).isPresent()){
            // Opcional: Poderia fazer um 'shift' automático aqui igual ao LessonService
            throw new ConflictException("Já existe um módulo na posição " + request.moduleOrder());
        }

        Module module = toEntity(request, course);
        return toResponseDTO(moduleRepository.save(module));
    }

    @Transactional
    public List<ModuleResponseDTO> reorder(Long courseId, List<ModuleReorderRequestDTO> requests) {
        if(!courseRepository.existsById(courseId)){
            throw new ResourceNotFoundException("Curso não encontrado.");
        }

        List<Module> currentModules = moduleRepository.findByCourseId(courseId);
        Map<Long, Module> moduleMap = currentModules.stream().collect(Collectors.toMap(Module::getId, m -> m));
        
        if(requests.stream().anyMatch(r -> !moduleMap.containsKey(r.moduleId()))){
            throw new BusinessRuleException("Tentativa de reordenar módulos que não pertencem a este curso.");
        }
        
        requests.forEach(req -> {
            Module module = moduleMap.get(req.moduleId());
            if(module != null) module.setModuleOrder(req.newOrder());
        });
        
        return moduleRepository.saveAll(currentModules).stream()
            .sorted(Comparator.comparing(Module::getModuleOrder)) 
            .map(this::toResponseDTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ModuleResponseDTO> getAllByCourseId(Long courseId) {
        return moduleRepository.findByCourseId(courseId).stream()
                .sorted(Comparator.comparing(Module::getModuleOrder))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // --- Métodos Auxiliares ---
    
    public Module findById(Long moduleId) {
        return moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Módulo não encontrado com ID: " + moduleId));
    }
    
    public ModuleResponseDTO getById(Long moduleId, Long courseId) {
        Module module = moduleRepository.findByIdAndCourseId(moduleId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Módulo não encontrado neste curso."));
        return toResponseDTO(module);
    }
    
    public boolean existsById(Long id) { return moduleRepository.existsById(id); }

    public Module findByCourseIdAndModuleOrder(Long courseId, int moduleOrder){
        return moduleRepository.findByCourseIdAndModuleOrder(courseId, moduleOrder)
                .orElseThrow(()-> new BusinessRuleException("Módulo sequencial não encontrado."));
    }

    public List<ModuleLessonCountDTO> getLessonCountsByModule(Long courseId) {
        return lessonRepository.countLessonsPerModuleInCourse(courseId).stream()
            .map(obj -> new ModuleLessonCountDTO((Long) obj[0], (Long) obj[1]))
            .toList();
    }
    
    public Module toEntity(ModuleRequestDTO request, Course course) {
        return Module.builder()
                .title(request.title()).description(request.description())
                .course(course).moduleOrder(request.moduleOrder()).build();
    }
    
    public ModuleResponseDTO toResponseDTO(Module module) {
        return new ModuleResponseDTO(
                module.getId(), module.getTitle(), module.getDescription(),
                module.getModuleOrder(), module.getCourse().getId(), module.getCourse().getTitle(),
                module.getLessons() != null ? module.getLessons().stream().map(lessonService::toResponseDTO).collect(Collectors.toSet()) : Collections.emptySet()
        );
    }
}