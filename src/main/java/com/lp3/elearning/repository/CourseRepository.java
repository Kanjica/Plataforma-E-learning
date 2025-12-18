package com.lp3.elearning.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lp3.elearning.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByTitle(String title);
    boolean existsById(Long id);
    // boolean existsByCategories(HashSet<Category> categories);
    List<Course> findByTitleContainingIgnoreCaseAndCategories_IdIn(String title, Set<Long> categoryIds);
    List<Course> findByTitleContainingIgnoreCase(String title);
    List<Course> findByCategories_IdIn(Set<Long> categoryIds);
}
