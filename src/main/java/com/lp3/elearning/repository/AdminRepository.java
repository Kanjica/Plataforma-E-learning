package com.lp3.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lp3.elearning.entities.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    
}
