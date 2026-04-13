package com.lp3.elearning.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.auth.InstructorRegisterDTO;
import com.lp3.elearning.dto.course.CourseResponseDTO;
import com.lp3.elearning.dto.user.InstructorResponseDTO;
import com.lp3.elearning.entities.Instructor;
import com.lp3.elearning.entities.UserRole;
import com.lp3.elearning.exception.ResourceNotFoundException;
import com.lp3.elearning.repository.InstructorRepository;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final CourseService courseService;
    private final AuthService authService;

    public InstructorService(InstructorRepository instructorRepository, CourseService courseService, AuthService authService) {
        this.instructorRepository = instructorRepository;
        this.courseService = courseService;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public InstructorResponseDTO createInstructor(InstructorRegisterDTO data) {
        authService.validateAndPrepare(data.email());

        Instructor instructor = Instructor.builder()
                .name(data.username())
                .email(data.email())
                .password(authService.encodePassword(data.password()))
                .role(UserRole.ROLE_INSTRUCTOR)
                .build();

        Instructor savedInstructor = instructorRepository.save(instructor);
        return toResponseDTO(savedInstructor);
    }
    
    public InstructorResponseDTO findById(Long id) {
        Instructor instructor = findInstructorEntityById(id);
        return toResponseDTO(instructor);
    }

    public Set<CourseResponseDTO> findCoursesByInstructorId(Long instructorId) {
        Instructor instructor = findInstructorEntityById(instructorId);
        return instructor.getCourses().stream()
            .map(course -> courseService.toResponseDTO(course))
            .collect(Collectors.toSet());
    }
    public Instructor findInstructorEntityById(Long id) {
        return instructorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Instrutor não encontrado com o ID: " + id));
    }
    
    // public InstructorResponse findInstructorById(Long id) {
    //     Instructor instructor = findInstructorEntityById(id);
    //     return new InstructorResponse(instructor.getId(), instructor.getName(), instructor.getEmail());
    // }

    public Set<Instructor> getInstructorsByValidIds(Set<Long> ids) {
        
        List<Long> requestedIds = ids.stream().toList(); 
        
        List<Instructor> foundInstructors = instructorRepository.findAllById(requestedIds);
        if (foundInstructors.size() != requestedIds.size()) {
            
            Set<Long> foundIds = foundInstructors.stream()
                .map(Instructor::getId)
                .collect(Collectors.toSet());

            String missingIds = requestedIds.stream()
                .filter(id -> !foundIds.contains(id))
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

            throw new ResourceNotFoundException("As seguintes IDs de Instrutor não foram encontradas: " + missingIds);
        }

        return new HashSet<>(foundInstructors);
    }

    public Set<InstructorResponseDTO> toResponseDTOs(Set<Instructor> instructors) {
        return instructors.stream()
            .map(instructor -> new InstructorResponseDTO(
                instructor.getId(),
                instructor.getName(),
                instructor.getEmail()
            ))
            .collect(Collectors.toSet());
    }

    public Set<Instructor> toEntitys(Set<Long> ids) {
        Set<Instructor> instructors = new HashSet<>();
        for (Long id : ids) {
            Instructor instructor = findInstructorEntityById(id);
            instructors.add(instructor);
        }
        return instructors;
    }
    
    public InstructorResponseDTO toResponseDTO(Instructor instructor) {
        return new InstructorResponseDTO(
            instructor.getId(),
            instructor.getName(),
            instructor.getEmail()
        );
    }
}
