package com.lp3.elearning.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "aulas_concluidas")
@Data @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AulaConcluida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @ManyToOne
    @JoinColumn(name = "matricula_id")
    private Matricula matricula;

    @ManyToOne
    @JoinColumn(name = "aula_id")
    private Aula aula;

    @NotNull
    @Column(name = "data_conclusao", nullable = false)
    private LocalDateTime dataConclusao;
}
