package org.nvtai.ProductionSchedulingSystem.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Roles")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long rid;

    String rname;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "RoleFeatures",
            joinColumns = @JoinColumn(name = "rid"),
            inverseJoinColumns = @JoinColumn(name = "fid")
    )
    @EqualsAndHashCode.Exclude
    Set<Feature> features = new HashSet<>();
}