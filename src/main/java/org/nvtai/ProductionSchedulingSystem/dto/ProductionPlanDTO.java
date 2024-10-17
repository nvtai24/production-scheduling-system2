package org.nvtai.ProductionSchedulingSystem.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductionPlanDTO {
    String planname;
    Date from;
    Date to;
    int departmentId;
    private Map<Integer, Integer> quantities;
    private Map<Integer, Float> efforts;
}