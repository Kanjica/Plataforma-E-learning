package com.lp3.elearning.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.catalina.manager.StatusManagerServlet;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "matriculas")
@Data @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @NotNull
    @Column(name = "data_matricula", nullable = false)
    private LocalDate dataMatricula;

    @Min(0) @Max(100)
    @Column(name = "progresso_geral", nullable = false)
    private Double progressoGeral;

    private StatusMatricula status;

    @OneToMany(mappedBy = "matricula", cascade = CascadeType.ALL)
    private Set<AulaConcluida> aulasConcluidas;
}
