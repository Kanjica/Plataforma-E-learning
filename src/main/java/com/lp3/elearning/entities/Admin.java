package com.lp3.elearning.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "admins")
@NoArgsConstructor @SuperBuilder
@Getter @Setter
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {
    
}
