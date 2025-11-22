package com.lp3.elearning.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Entity
@Table(name = "alunos")
@Data @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @NotBlank(message = "Nome do aluno não pode estar vazio")
    @Size(max=100)
    @Column(nullable = false, length = 100)
    private String nome;

    @Email
    @NotBlank(message = "Email do aluno não pode estar vazio ")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Senha do aluno não pode estar vazia")
    private String senha;

    @OneToMany(mappedBy = "aluno")
    private Set<Matricula> matriculas;
}
