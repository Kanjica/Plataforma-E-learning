package com.lp3.elearning.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "aulas")
@Data @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @NotBlank(message = "O título da aula não pode ser vazio")
    @Size(min = 5, max = 150)
    @Column(nullable = false, length = 150)
    private String titulo;

    @NotBlank(message = "URL não pode estár vazia")
    @Column(name = "url_video",nullable = false)
    private String urlVideo;

    @ManyToOne
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;

    @NotNull
    @Min(1)
    private Integer ordem;
}
