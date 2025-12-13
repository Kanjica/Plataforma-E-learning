package com.lp3.elearning.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "respostas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @NotNull
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id", nullable = false)
    private Topico topico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resposta_pai_id")
    private Resposta respostaPai; // Referência para a resposta que ela está respondendo

    // Relação 1:N com as Respostas-Filhas (para buscar o thread)
    @OneToMany(mappedBy = "respostaPai", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Resposta> respostasFilhas;
}