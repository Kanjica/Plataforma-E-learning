package com.lp3.elearning.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lp3.elearning.dto.auth.InstructorRegisterDTO;
import com.lp3.elearning.dto.user.InstructorResponseDTO;
import com.lp3.elearning.entities.Instructor;
import com.lp3.elearning.entities.UserRole;
import com.lp3.elearning.exception.ResourceNotFoundException;
import com.lp3.elearning.mapper.InstructorMapper;
import com.lp3.elearning.repository.InstructorRepository;
import com.lp3.elearning.security.anottation.Auditable;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final AuthService authService;
    private final InstructorMapper instructorMapper;
    
    @Transactional(readOnly = true)
    @Auditable(action = "CRIAR_INSTRUTOR")
    public InstructorResponseDTO create(InstructorRegisterDTO data) {
        authService.validateAndPrepare(data.email());

        Instructor instructor = Instructor.builder()
                .name(data.name())
                .email(data.email())
                .password(authService.encodePassword(data.password()))
                .role(UserRole.ROLE_INSTRUCTOR)
                .build();

        Instructor savedInstructor = instructorRepository.save(instructor);
        return instructorMapper.toResponseDTO(savedInstructor);
    }
    
    public InstructorResponseDTO findById(Long id) {
        Instructor instructor = findInstructorEntityById(id);
        return instructorMapper.toResponseDTO(instructor);
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

}
