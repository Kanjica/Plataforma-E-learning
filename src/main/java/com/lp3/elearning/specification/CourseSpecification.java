package com.lp3.elearning.specification;

import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.lp3.elearning.entities.Category_;
import com.lp3.elearning.entities.Course;
import com.lp3.elearning.entities.Course_;

public class CourseSpecification {
    
    public static Specification<Course> hasTitle(String title) {
        return (root, query, cb) -> {
            if (title == null || title.isBlank()) return null;
            return cb.like(cb.lower(root.get(Course_.title)), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<Course> hasCategoryIds(Set<Long> categoryIds) {
        return (root, query, cb) -> {
            if (categoryIds == null || categoryIds.isEmpty()) return null;
            query.distinct(true);
            return root.join(Course_.categories).get(Category_.id).in(categoryIds);
        };
    }
}
