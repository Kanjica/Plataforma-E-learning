package com.lp3.elearning.service;

import com.lp3.elearning.repository.AlunoRepository;
import com.lp3.elearning.repository.InstrutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserDetails user = alunoRepository.findByEmail(login);
        if (user != null) return user;
        
        user = instrutorRepository.findByEmail(login);
        if (user != null) return user;

        throw new UsernameNotFoundException("Usuário não encontrado.");
    }
}