package org.nvtai.ProductionSchedulingSystem.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer uid;

    @ManyToOne
    @JoinColumn(name = "eid")
    Employee employee;

    String username;
    String password;
    Boolean active;
    String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "UserRoles",
            joinColumns = @JoinColumn(name = "uid"),
            inverseJoinColumns = @JoinColumn(name = "rid")
    )
    Set<Role> roles = new HashSet<>();

}