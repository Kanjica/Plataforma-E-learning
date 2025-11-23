package com.lp3.elearning.repository;

import com.lp3.elearning.entities.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    UserDetails findByEmail(String login);
}
