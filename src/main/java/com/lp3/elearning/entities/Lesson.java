package com.lp3.elearning.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lessons")
@Data @AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "O título da aula não pode ser vazio")
    @Size(min = 5, max = 150)
    @Column(nullable = false, length = 150)
    private String title;

    @NotBlank(message = "O conteúdo da aula não pode ser vazio")
    @Column(nullable = false)   
    private String content;

    @NotBlank(message = "URL não pode estár vazia")
    @Column(name = "video_url",nullable = false)
    private String videoUrl;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @NotNull
    @Min(1)
    @Column(name = "lesson_order", nullable = false)
    private Integer lessonOrder;
}
