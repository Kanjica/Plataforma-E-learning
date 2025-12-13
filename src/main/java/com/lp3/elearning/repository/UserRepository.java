package com.lp3.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lp3.elearning.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
}
