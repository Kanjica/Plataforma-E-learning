package com.lp3.elearning.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "modules")
@Data @AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @NotBlank(message = "O título do módulo não pode ser vazio.")
    @Size(min = 5, max = 100)
    @Column(nullable = false, length = 100)
    private String title;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
    private List<Lesson> lessons;

    @NotNull
    @Min(1)
    @Column(name = "module_order", nullable = false)
    private Integer moduleOrder;
}
