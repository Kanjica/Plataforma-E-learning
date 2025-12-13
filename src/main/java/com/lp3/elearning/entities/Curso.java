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
import java.util.Set;

/**
 * Entidade que representa um Curso na plataforma de E-learning.
 * Mapeia para a tabela 'cursos' no db.
 * * @author Jão
 */

/*
* @Data traz algumas anotações que basicamente dizem que a classe é uma classe de dados
* @AllArgsConstructor diz que a classe tem um construtor que recebe todos os argumentos
*
* @Entity diz que a classe é uma Entidade no db
* @Table(name = "cursos" ta dizendo que o nome da tabela vai ser "cursos"
* */
@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "cursos")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    //Chave gerada automaticamente
    private long id;

    @NotBlank(message = "O título do curso não pode ser vazio.")
    @Size(min = 5, max = 150)
    @Column(nullable = false, length = 150)
    /*
    * Além das validações com as tags (@NotBlank, @Size, etc.) tá setando no db tambem, com (nullable = false, length = 100)
    * é uma validação dupla basicamente
    * */
    private String titulo;

    @NotBlank(message = "A descrição do curso não pode ser vazia.")
    @Size(min = 20)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String descricao;

    @NotNull(message = "O campo é obrigatório.")
    @Min(value = 10, message = "A carga horária mínima para um curso é de 10 horas.")
    @Column(name = "carga_horaria", nullable = false)
    private Integer cargaHoraria;

    @ManyToMany
    @JoinTable(
            name = "curso_instrutor", // nome da tabela de junção
            joinColumns = @JoinColumn(name = "curso_id"), // FK que referencia esta tabela
            inverseJoinColumns = @JoinColumn(name = "instrutor_id") // FK que referencia a tabela instrutores
    )
    private Set<Instrutor> instrutores;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    /*
    * CascadeType.ALL Significa que se você deletar o curso, os módulos associados a ele também serão deletados junto
    * */
    private List<Modulo> modulos;

}
