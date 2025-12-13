package com.lp3.elearning.service;

import com.lp3.elearning.repository.InstructorRepository;
import com.lp3.elearning.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserDetails user = studentRepository.findByEmail(login);
        if (user != null) return user;

        user = instructorRepository.findByEmail(login);
        if (user != null) return user;

        throw new UsernameNotFoundException("Usuário não encontrado.");
    }
}