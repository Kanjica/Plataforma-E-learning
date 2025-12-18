package com.lp3.elearning.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lp3.elearning.entities.Lesson;;

public interface LessonRepository extends JpaRepository<Lesson, Long>{
    List<Lesson> findByModuleId(Long moduleId);
    Optional<Lesson> findByIdAndModuleId(Long lessonId, Long moduleId);

    /**
     * Retorna a contagem total de aulas para um curso específico.
     * @param courseId ID do curso.
     * @return O número total de aulas.
     */
    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.module.course.id = :courseId")
    Long countLessonsByCourseId(Long courseId);

    // OU, se você quiser retornar a contagem para todos os cursos:
    
    /**
     * Retorna uma lista de pares [courseId, totalLessons] para todos os cursos.
     */
    @Query("SELECT l.module.course.id, COUNT(l) FROM Lesson l GROUP BY l.module.course.id")
    List<Object[]> countLessonsForAllCourses();

    /**
     * Retorna a contagem de aulas por módulo para um curso específico.
     * @param courseId ID do curso.
     * @return Uma lista de pares [moduleId, lessonCount].
     */
    @Query("SELECT l.module.id, COUNT(l) FROM Lesson l WHERE l.module.course.id = :courseId GROUP BY l.module.id")
    List<Object[]> countLessonsPerModuleInCourse(Long courseId);
    Optional<Lesson> findByModuleIdAndLessonOrder(Long moduleId, Integer lessonOrder);
    boolean existsByLessonOrderAndModuleId(Integer order, Long moduleId);
    Optional<Lesson> findByLessonOrder(Integer lessonOrder);
    Optional<Lesson> findFirstByModuleIdOrderByLessonOrderDesc(Long moduleId);
    
}
