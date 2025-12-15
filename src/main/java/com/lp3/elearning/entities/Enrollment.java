package com.lp3.elearning.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "enrollments")
@Data @AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @NotNull
    @Builder.Default
    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate = LocalDate.now();

    @Min(0) @Max(100)
    @Builder.Default
    @Column(name = "overall_progress", nullable = false)
    private Double overallProgress = 0.0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusEnrollment status = StatusEnrollment.IN_PROGRESS;

    @Builder.Default
    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL)
    private Set<CompletedLesson> completedLessons = new HashSet<>();
}
