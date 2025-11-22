package com.lp3.elearning.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Table(name = "modulos")
@Data @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @NotBlank(message = "O título do módulo não pode ser vazio.")
    @Size(min = 5, max = 100)
    @Column(nullable = false, length = 100)
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @OneToMany(mappedBy = "modulo", cascade = CascadeType.ALL)
    private List<Aula> aulas;

    @NotNull
    @Min(1)
    private Integer ordem;
}
