package com.lp3.elearning.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.ModuleReorderRequestDTO;
import com.lp3.elearning.dto.ModuleRequestDTO;
import com.lp3.elearning.dto.ModuleResponseDTO;
import com.lp3.elearning.entities.Course;
import com.lp3.elearning.repository.ModuleRepository;

import jakarta.transaction.Transactional;

import com.lp3.elearning.entities.Module;
import com.lp3.elearning.exception.BusinessRuleException;
import com.lp3.elearning.exception.ConflictException;

@Service
public class ModuleService {

    private final CourseService courseService;
    private final ModuleRepository moduleRepository;

    public ModuleService(CourseService courseService, ModuleRepository moduleRepository) {
        this.courseService = courseService;
        this.moduleRepository = moduleRepository;
    }
    

    public ModuleResponseDTO create(ModuleRequestDTO request, Long courseId){
    
        // 1. Busca o curso (pode lançar 404 se não existir)
        Course course = courseService.getCourseById(courseId);

        Optional<Module> existingModuleByTitle = moduleRepository.findByTitleAndCourseId(request.title(), courseId);
        
        if(existingModuleByTitle.isPresent()){
            ModuleResponseDTO existingModuleDTO = toResponseDTO(existingModuleByTitle.get());
            throw new ConflictException("Módulo com o título '" + request.title() + 
                                    "' já existe nesse curso.\nDetalhes: " + existingModuleDTO);
        }

        Optional<Module> existingModuleByOrder = moduleRepository.findByModuleOrderAndCourseId(request.moduleOrder(), courseId);

        if(existingModuleByOrder.isPresent()){
            ModuleResponseDTO existingModuleDTO = toResponseDTO(existingModuleByOrder.get());
            throw new ConflictException("Módulo com a ordem '" + request.moduleOrder() + 
                                    "' já existe nesse curso.\nDetalhes: " + existingModuleDTO);
        }

        Module module = toEntity(request, course);

        return toResponseDTO(moduleRepository.save(module));
    }

    @Transactional
    public List<ModuleResponseDTO> reorder(Long courseId, List<ModuleReorderRequestDTO> requests) {
        
        if(!courseService.existsById(courseId)){
            throw new BusinessRuleException("Curso com ID " + courseId + " não encontrado.");
        }

        if(requests.isEmpty()){
            throw new IllegalArgumentException("A lista de reordenação não pode ser vazia.");
        }

        List<Module> currentModules = moduleRepository.findByCourseId(courseId);
        
        Map<Long, Module> moduleMap = currentModules.stream()
            .collect(Collectors.toMap(Module::getId, module -> module));
        
        if(requests.size() != currentModules.size() || 
            requests.stream().anyMatch(r -> !moduleMap.containsKey(r.moduleId()))){
            throw new BusinessRuleException("A lista de IDs para reordenação está incompleta ou contém IDs inválidos.");
        }
        
        requests.forEach(request -> {
            Module moduleToUpdate = moduleMap.get(request.moduleId());
            moduleToUpdate.setModuleOrder(request.newOrder()); 
        });
        
        List<Module> updatedModules = moduleRepository.saveAll(currentModules);
        
        return updatedModules.stream()
            .sorted(Comparator.comparing(Module::getModuleOrder)) 
            .map(this::toResponseDTO)
            .toList();
    }
    public Module toEntity(ModuleRequestDTO request, Course course) {
        return Module.builder()
                .title(request.title())
                .description(request.description())
                .course(course)
                .moduleOrder(request.moduleOrder())
                .build();
    }

    public ModuleResponseDTO toResponseDTO(Module module) {
        return new ModuleResponseDTO(
                module.getId(),
                module.getTitle(),
                module.getDescription(),
                module.getModuleOrder(),
                module.getCourse().getId(),
                module.getCourse().getTitle()
        );
    }
}
