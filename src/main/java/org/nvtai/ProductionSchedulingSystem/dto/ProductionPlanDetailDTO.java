package org.nvtai.ProductionSchedulingSystem.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.nvtai.ProductionSchedulingSystem.entity.Department;
import org.nvtai.ProductionSchedulingSystem.entity.Employee;
import org.nvtai.ProductionSchedulingSystem.entity.Plan;

import java.sql.Date;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductionPlanDetailDTO {

    Plan plan;

    Department department;
    String managerName;

    String note;

    List<ProductDetailDTO> productDetails; // plan Header

    private List<DailyProductionDTO> dailyProductions; // plan details

    public ProductDetailDTO getProductDetailByPid(Integer productId) {
        return productDetails.stream()
                .filter(detail -> detail.getProductId() == productId)
                .findFirst()
                .orElse(null);
    }

}