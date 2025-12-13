package com.lp3.elearning.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "topicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Max(200)
    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @NotNull
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    // Relação N:1 com o Curso onde o tópico foi criado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    // Relação N:1 com o Usuário (aluno ou instrutor) que criou o tópico
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relação 1:N com as Respostas (Comentários)
    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Resposta> respostas;
}