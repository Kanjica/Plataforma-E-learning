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
@Table(name = "instructors")
@NoArgsConstructor 
@SuperBuilder 
@Setter 
@Getter 
@EqualsAndHashCode(callSuper = true) 
@PrimaryKeyJoinColumn(name = "user_id")
public class Instructor extends User {

    @Builder.Default
    @ManyToMany(mappedBy = "instructors") 
    private Set<Course> courses = new HashSet<>();
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"));
    }

}
