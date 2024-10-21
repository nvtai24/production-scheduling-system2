package org.nvtai.ProductionSchedulingSystem.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Table(name = "Employees")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer eid;

    String ename;
    String phoneNumber;
    String address;

    @ManyToOne
    @JoinColumn(name = "did")
    Department department;

    @ManyToOne
    @JoinColumn(name = "sid")
    Salary salary;

    Boolean ismanager = false;

    Boolean isdeleted = false;
}