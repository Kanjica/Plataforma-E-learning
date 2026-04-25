package com.lp3.elearning.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.lp3.elearning.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    boolean existsByTitle(String title);
    boolean existsById(Long id);
    boolean existsByIdAndInstructorsId(Long courseId, Long instructorId);
    // boolean existsByCategories(HashSet<Category> categories);
    List<Course> findByTitleContainingIgnoreCaseAndCategories_IdIn(String title, Set<Long> categoryIds);
    List<Course> findByTitleContainingIgnoreCase(String title);
    List<Course> findByCategories_IdIn(Set<Long> categoryIds);
    boolean existsByModulesIdAndInstructorsId(Long moduleId, Long instructorId);
}
