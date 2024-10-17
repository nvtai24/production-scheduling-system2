package org.nvtai.ProductionSchedulingSystem.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Entity
@Table(name = "Plans")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer plid;

    String plname;
    Date startdate;
    Date enddate;

    @ManyToOne
    @JoinColumn(name = "did")
    Department department;
}