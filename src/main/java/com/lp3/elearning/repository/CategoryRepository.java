package com.lp3.elearning.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.lp3.elearning.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
