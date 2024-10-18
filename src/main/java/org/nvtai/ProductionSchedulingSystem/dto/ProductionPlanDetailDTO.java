package org.nvtai.ProductionSchedulingSystem.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductionPlanDetailDTO {
    int planId;
    String planName;
    Date startDate;
    Date endDate;

    String departmentName;
    private String managerName;

    List<ProductDetailDTO> productDetails;

    private List<DailyProductionDTO> dailyProductions;
}