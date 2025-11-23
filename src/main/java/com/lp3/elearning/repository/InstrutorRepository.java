package com.lp3.elearning.repository;

import com.lp3.elearning.entities.Instrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrutorRepository extends JpaRepository<Instrutor, Long> {
    UserDetails findByEmail(String login);
}
