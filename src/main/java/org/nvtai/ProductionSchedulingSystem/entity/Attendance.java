package org.nvtai.ProductionSchedulingSystem.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "Attendance")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer atid;

    @ManyToOne
    @JoinColumn(name = "waid")
    WorkAssignment workAssignment;

    Integer actualquantity;

    Float alpha;

    String note;
}