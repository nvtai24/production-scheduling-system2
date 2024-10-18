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

    List<ProductDetailDTO> productDetails;

    private List<DailyProductionDTO> dailyProductions;


    public ProductDetailDTO getProductDetailByPid(Integer productId) {
        return productDetails.stream()
                .filter(detail -> detail.getProductId() == productId)
                .findFirst()
                .orElse(null);
    }

}