package org.nvtai.ProductionSchedulingSystem.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "PlanHeaders")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer phid;

    @ManyToOne
    @JoinColumn(name = "plid")
    Plan plan;

    @ManyToOne
    @JoinColumn(name = "pid")
    Product product;

    int quantity;

    float estimatedeffort;
}