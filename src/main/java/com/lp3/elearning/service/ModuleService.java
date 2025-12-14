package com.lp3.elearning.service;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.ModuleRequestDTO;
import com.lp3.elearning.dto.ModuleResponseDTO;
import com.lp3.elearning.entities.Course;
import com.lp3.elearning.repository.ModuleRepository;
import com.lp3.elearning.entities.Module;

@Service
public class ModuleService {

    private final CourseService courseService;
    private final ModuleRepository moduleRepository;

    public ModuleService(CourseService courseService, ModuleRepository moduleRepository) {
        this.courseService = courseService;
        this.moduleRepository = moduleRepository;
    }
    

    public ModuleResponseDTO create(ModuleRequestDTO request, Long courseId) {

        Course course = courseService.getCourseById(courseId);

        // VERIFICAR SE JÁ EXISTE MODULO COM MESMO TÍTULO NO CURSO E SE TEM MODULO COM MESMO ORDEM
        Module module = toEntity(request, course);

        return toResponseDTO(moduleRepository.save(module));
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
