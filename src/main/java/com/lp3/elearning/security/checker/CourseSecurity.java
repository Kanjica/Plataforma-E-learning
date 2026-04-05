package com.lp3.elearning.security.checker;

import org.springframework.stereotype.Component;

import com.lp3.elearning.repository.CourseRepository;
import com.lp3.elearning.repository.LessonRepository;

@Component("courseSecurity") 
public class CourseSecurity {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    public CourseSecurity(CourseRepository courseRepository, LessonRepository lessonRepository) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
    }

    public boolean isInstructorOfCourse(Long courseId, Long userId) {
        return courseRepository.existsByIdAndInstructorsId(courseId, userId);
    }

    public boolean isInstructorOfLesson(Long lessonId, Long userId) {
        return lessonRepository.existsByIdAndModuleCourseInstructorsId(lessonId, userId);
    }

    public boolean isInstructorOfModule(Long moduleId, Long userId) {
        return courseRepository.existsByModulesIdAndInstructorsId(moduleId, userId);
    }

}