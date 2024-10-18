package org.nvtai.ProductionSchedulingSystem.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductionPlanDTO {
    int planId;
    String planname;
    Date startdate;
    Date enddate;
    int departmentId;
    Map<Integer, Integer> quantities;
    Map<Integer, Float> efforts;
    String note;
}