package org.nvtai.ProductionSchedulingSystem.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "WorkAssignments")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer waid;

    @ManyToOne
    @JoinColumn(name = "pdid")
    PlanDetail planDetail;

    Integer eid;

    Integer quantity;
}