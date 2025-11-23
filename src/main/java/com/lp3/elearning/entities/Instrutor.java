package com.lp3.elearning.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "instrutores")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Instrutor implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    @NotBlank(message = "Nome do instrutor não pode estar vazio")
    @Size(max=100)
    @Column(nullable = false, length = 100)
    private String nome;

    @Email
    @NotBlank(message = "Email do instrutor não pode estar vazio ")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Senha do instrutor não pode estar vazia")
    private String senha;

    @ManyToMany(mappedBy = "instrutores") // Mapeado pelo campo 'instrutores' na classe Curso
    private Set<Curso> cursos;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_INSTRUTOR"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return senha;
    }
}
