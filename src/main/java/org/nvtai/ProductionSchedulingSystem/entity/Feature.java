package org.nvtai.ProductionSchedulingSystem.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Features")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long fid;

    String fname;

    String url;

    @ManyToMany(mappedBy = "features")
    @EqualsAndHashCode.Exclude
    Set<Role> roles = new HashSet<>();
}