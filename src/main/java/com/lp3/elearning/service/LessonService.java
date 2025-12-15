package com.lp3.elearning.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.LessonReorderRequestDTO;
import com.lp3.elearning.dto.LessonRequestDTO;
import com.lp3.elearning.dto.LessonResponseDTO;
import com.lp3.elearning.entities.Lesson;
import com.lp3.elearning.repository.LessonRepository;
import com.lp3.elearning.entities.Module;
import com.lp3.elearning.exception.BusinessRuleException;

import jakarta.transaction.Transactional;

@Service
public class LessonService {
    
    private final LessonRepository lessonRepository;
    private final ModuleService moduleService;

    public LessonService(LessonRepository lessonRepository, ModuleService moduleService) {
        this.lessonRepository = lessonRepository;
        this.moduleService = moduleService;
    }

    public LessonResponseDTO create(LessonRequestDTO lessonRequest) {
        Long moduleId = lessonRequest.moduleId();
        
        Module module = moduleService.findById(moduleId);
        
        Lesson lesson = toEntity(lessonRequest, module);
        
        return toResponseDTO(lessonRepository.save(lesson));
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
}
