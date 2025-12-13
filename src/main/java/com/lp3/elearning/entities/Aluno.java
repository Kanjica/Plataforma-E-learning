package com.lp3.elearning.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "alunos")
@NoArgsConstructor
@SuperBuilder 
@Getter @Setter 
@EqualsAndHashCode(callSuper = true)
public class Aluno extends Usuario {

    @Builder.Default
    @OneToMany(mappedBy = "aluno")
    private Set<Matricula> matriculas = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ALUNO"));
    }

}
