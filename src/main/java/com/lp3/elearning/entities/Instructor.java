package com.lp3.elearning.entities;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "instructors")
@NoArgsConstructor 
@SuperBuilder 
@Setter 
@Getter 
@EqualsAndHashCode(callSuper = true) 
public class Instructor extends User {

    @Builder.Default
    @ManyToMany(mappedBy = "instructors") 
    private Set<Course> courses = new HashSet<>();

}
