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

    // Injete os repositórios reais (AlunoRepository e InstrutorRepository)
    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private InstrutorRepository instrutorRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // Tenta encontrar como ALUNO (assumindo que o login é o email)
        UserDetails aluno = alunoRepository.findByEmail(login);

        if (aluno != null) {
            return aluno;
        }

        UserDetails instrutor = instrutorRepository.findByEmail(login);

        if (instrutor != null) {
            return instrutor;
        }

        throw new UsernameNotFoundException("Usuário não encontrado.");
    }
}