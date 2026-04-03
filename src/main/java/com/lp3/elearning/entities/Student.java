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
@Table(name = "students")
@NoArgsConstructor
@SuperBuilder 
@Getter @Setter 
@EqualsAndHashCode(callSuper = true)
public class Student extends User {

    @Builder.Default
    @OneToMany(mappedBy = "student")
    private Set<Enrollment> enrollments = new HashSet<>();

}
