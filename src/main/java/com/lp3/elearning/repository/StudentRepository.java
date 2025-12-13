package com.lp3.elearning.repository;

import com.lp3.elearning.entities.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    UserDetails findByEmail(String login);
}
