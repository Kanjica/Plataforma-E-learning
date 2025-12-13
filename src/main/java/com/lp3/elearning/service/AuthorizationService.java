package com.lp3.elearning.service;

import com.lp3.elearning.repository.InstructorRepository;
import com.lp3.elearning.repository.StudentRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private StudentRepository studentRepository;
    private InstructorRepository instructorRepository;

    public AuthorizationService(StudentRepository studentRepository, InstructorRepository instructorRepository) {
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserDetails user = studentRepository.findByEmail(login);
        if (user != null) return user;

        user = instructorRepository.findByEmail(login);
        if (user != null) return user;

        throw new UsernameNotFoundException("Usuário não encontrado.");
    }
}