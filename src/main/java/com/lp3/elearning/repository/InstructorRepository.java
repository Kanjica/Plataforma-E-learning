package com.lp3.elearning.repository;

import com.lp3.elearning.entities.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    UserDetails findByEmail(String login);
}
