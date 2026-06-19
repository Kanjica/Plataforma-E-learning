package com.lp3.elearning.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.course.ModuleLessonCountDTO;
import com.lp3.elearning.dto.course.ModuleReorderRequestDTO;
import com.lp3.elearning.dto.course.ModuleRequestDTO;
import com.lp3.elearning.dto.course.ModuleResponseDTO;
import com.lp3.elearning.entities.Course;
import com.lp3.elearning.entities.Module;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.exception.ConflictException;
import com.lp3.elearning.exception.ResourceNotFoundException;
import com.lp3.elearning.mapper.ModuleMapper;
import com.lp3.elearning.repository.CourseRepository;
import com.lp3.elearning.repository.LessonRepository;
import com.lp3.elearning.repository.ModuleRepository;
import com.lp3.elearning.security.anottation.Auditable;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "modules")
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final ModuleMapper moduleMapper;

    @Transactional
    @Auditable(action = "CRIAR_MÓDULO")
    @CacheEvict(cacheNames = "courses", key = "#courseId") 
    public ModuleResponseDTO create(ModuleRequestDTO request, Long courseId){
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado."));

        // Valida duplicidade de título no mesmo curso
        if(moduleRepository.findByTitleAndCourseId(request.title(), courseId).isPresent()){
            throw new ConflictException("Módulo com o título '" + request.title() + "' já existe neste curso.");
        }

        // Valida duplicidade de ordem
        if(moduleRepository.findByModuleOrderAndCourseId(request.moduleOrder(), courseId).isPresent()){
            // Opcional: Poderia fazer um 'shift' automático aqui igual ao LessonService
            throw new ConflictException("Já existe um módulo na posição " + request.moduleOrder());
        }

        Module module = moduleMapper.toEntity(request);
        module.setCourse(course);

        return moduleMapper.toResponseDTO(moduleRepository.save(module));
    }

    @Transactional
    @Auditable(action = "REORDENAR_MÓDULOS")
    @Caching(evict = {
        @CacheEvict(cacheNames = "modules", key = "'courseId-' + #courseId"),
        @CacheEvict(cacheNames = "courses", key = "#courseId")
    })
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
            .map(moduleMapper::toResponseDTO)
            .toList();
    }
    
    @Transactional
    @Auditable(action = "ATUALIZAR_MÓDULO")
    public ModuleResponseDTO update(Long moduleId, ModuleRequestDTO request) {
        Module module = findById(moduleId);

        // Valida duplicidade de título no mesmo curso (excluindo o próprio módulo)
        if(moduleRepository.findByTitleAndCourseId(request.title(), module.getCourse().getId())
            .filter(m -> !m.getId().equals(moduleId)).isPresent()){
            throw new ConflictException("Módulo com o título '" + request.title() + "' já existe neste curso.");
        }

        // Valida duplicidade de ordem (excluindo o próprio módulo)
        if(moduleRepository.findByModuleOrderAndCourseId(request.moduleOrder(), module.getCourse().getId())
            .filter(m -> !m.getId().equals(moduleId)).isPresent()){
            throw new ConflictException("Já existe um módulo na posição " + request.moduleOrder());
        }

        module.setTitle(request.title());
        module.setDescription(request.description());
        module.setModuleOrder(request.moduleOrder());

        Long courseId = module.getCourse().getId();

        ModuleResponseDTO response =  moduleMapper.toResponseDTO(module); 
        this.executeEvictModule(moduleId, courseId);

        return response;
    }

    @Transactional
    @Auditable(action = "DELETAR_MÓDULO")
    public void delete(Long moduleId) {
        Module module = findById(moduleId);
        moduleRepository.delete(module);

        // Reorganiza para não deixar "buracos" na numeração (ex: 1, 3, 4 vira 1, 2, 3)
        List<Module> remainingModules = moduleRepository.findByCourseId(module.getCourse().getId());
        remainingModules.stream()
            .filter(m -> m.getModuleOrder() > module.getModuleOrder())
            .forEach(m -> m.setModuleOrder(m.getModuleOrder() - 1));

        Long courseId = module.getCourse().getId();
        moduleRepository.saveAll(remainingModules);
        
        this.executeEvictModule(moduleId, courseId);
    }

    @Transactional(readOnly = true)
    @Cacheable(key = "'courseId-' + #courseId")
    public List<ModuleResponseDTO> getAllByCourseId(Long courseId) {
        return moduleRepository.findByCourseId(courseId).stream()
                .sorted(Comparator.comparing(Module::getModuleOrder))
                .map(moduleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Module findById(Long moduleId) {
        return moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Módulo não encontrado com ID: " + moduleId));
    }
    
    @Cacheable(key = "#moduleId")
    public ModuleResponseDTO getById(Long moduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Módulo não encontrado com ID: " + moduleId));
        return moduleMapper.toResponseDTO(module);
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

    @Caching(evict = {
        @CacheEvict(key = "#moduleId"),
        @CacheEvict(key = "'courseId-' + #courseId"), 
        @CacheEvict(cacheNames="courses", key = "#courseId")
    })
    public void executeEvictModule(Long moduleId, Long courseId) {}
    
}