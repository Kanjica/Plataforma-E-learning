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
@Table(name = "instrutores")
@NoArgsConstructor 
@SuperBuilder 
@Setter 
@Getter 
@EqualsAndHashCode(callSuper = true) 
public class Instrutor extends Usuario {

    @Builder.Default
    @ManyToMany(mappedBy = "instrutores") 
    private Set<Curso> cursos = new HashSet<>();
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_INSTRUTOR"));
    }

}
