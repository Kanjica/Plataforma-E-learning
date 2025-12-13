package com.lp3.elearning.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// para garantir que um Aluno só avalie o Curso uma vez
@Table(name = "avaliacoes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"aluno_id", "curso_id"})
})
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(0)
    @Max(5)
    @Column(nullable = false)
    private Integer nota;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime dataAvaliacao = LocalDateTime.now();

    // Relação N:1 com o Aluno que fez a avaliação
    // (Assumindo que apenas Alunos avaliam, não Instrutores)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno; 

    // Relação N:1 com o Curso que foi avaliado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;
}