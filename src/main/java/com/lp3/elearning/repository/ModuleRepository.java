package com.lp3.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lp3.elearning.entities.Module;

public interface ModuleRepository extends JpaRepository<Module, Long>{
    boolean existsByTitleAndCourseId(String title, Long courseId);
    boolean existsByModuleOrderAndCourseId(Integer moduleOrder, Long courseId);
}
