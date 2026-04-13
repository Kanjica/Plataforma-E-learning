package com.lp3.elearning.service;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.auth.StudentRegisterDTO;
import com.lp3.elearning.dto.user.StudentResponseDTO;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.entities.UserRole;
import com.lp3.elearning.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class StudentService {
    
    private final StudentRepository studentRepository;
    private final AuthService authService;

    public StudentService(StudentRepository studentRepository, AuthService authService) {
        this.studentRepository = studentRepository;
        this.authService = authService;
    }

    @Transactional
    public void createStudent(StudentRegisterDTO data) {
        authService.validateAndPrepare(data.email());

        Student student = Student.builder()
                .name(data.username())
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
        return new StudentResponseDTO(
            student.getId(),
            student.getName(),
            student.getEmail()
        );
    }

    public StudentResponseDTO toResponseDTO(Student student){
        return new StudentResponseDTO(
            student.getId(),
            student.getName(),
            student.getEmail()
        );
    }
}
