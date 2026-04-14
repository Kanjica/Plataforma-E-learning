package com.lp3.elearning.service;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.auth.StudentRegisterDTO;
import com.lp3.elearning.dto.user.StudentResponseDTO;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.entities.UserRole;
import com.lp3.elearning.mapper.StudentMapper;
import com.lp3.elearning.repository.StudentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {
    
    private final StudentRepository studentRepository;
    private final AuthService authService;
    private final StudentMapper studentMapper;

    @Transactional
    public void createStudent(StudentRegisterDTO data) {
        authService.validateAndPrepare(data.email());

        Student student = Student.builder()
                .name(data.name())
                .email(data.email())
                .password(authService.encodePassword(data.password()))
                .role(UserRole.ROLE_STUDENT)
                .build();

        studentRepository.save(student);
    }
    
    public Student findById(Long id) {
        return studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public StudentResponseDTO findByIdResponseDTO(Long id) {
        Student student = findById(id);
        return studentMapper.toResponseDTO(student);
    }
}
