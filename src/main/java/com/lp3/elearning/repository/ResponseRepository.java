package com.lp3.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lp3.elearning.entities.Response;

public interface ResponseRepository extends JpaRepository<Response, Long>{
    
}
