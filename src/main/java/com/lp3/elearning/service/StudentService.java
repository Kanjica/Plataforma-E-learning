package com.lp3.elearning.service;

import org.springframework.stereotype.Service;

import com.lp3.elearning.dto.StudentResponseDTO;
import com.lp3.elearning.entities.Student;
import com.lp3.elearning.repository.StudentRepository;

@Service
public class StudentService {
    
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
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
}
