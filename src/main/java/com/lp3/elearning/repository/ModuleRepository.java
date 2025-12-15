package com.lp3.elearning.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lp3.elearning.entities.Module;

public interface ModuleRepository extends JpaRepository<Module, Long>{
    boolean existsByTitleAndCourseId(String title, Long courseId);
    boolean existsByModuleOrderAndCourseId(Integer moduleOrder, Long courseId);
    boolean existsById(Long id);
    List<Module> findByCourseId(Long courseId);
    Optional<Module> findByTitleAndCourseId(String title, Long courseId);
    Optional<Module> findByModuleOrderAndCourseId(Integer moduleOrder, Long courseId);
    Optional<Module> findByIdAndCourseId(Long moduleId, Long courseId);
}
