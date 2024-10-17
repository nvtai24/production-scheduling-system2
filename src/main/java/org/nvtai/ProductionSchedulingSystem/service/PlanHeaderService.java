package org.nvtai.ProductionSchedulingSystem.service;

import org.nvtai.ProductionSchedulingSystem.dto.ProductionPlanDTO;
import org.nvtai.ProductionSchedulingSystem.entity.Plan;
import org.nvtai.ProductionSchedulingSystem.entity.PlanHeader;
import org.nvtai.ProductionSchedulingSystem.entity.Product;
import org.nvtai.ProductionSchedulingSystem.mapper.PlanMapper;
import org.nvtai.ProductionSchedulingSystem.repository.PlanHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanHeaderService {

    @Autowired
    private PlanHeaderRepository planHeaderRepository;

    @Autowired
    private ProductService productService;

    public void createPlanHeader(ProductionPlanDTO productionPlanDTO, Plan plan) {
        for (Integer productId : productionPlanDTO.getQuantities().keySet()) {

            Integer rawQuantity = productionPlanDTO.getQuantities().get(productId);
            Float rawEffort = productionPlanDTO.getEfforts().get(productId);

            int quantity = (rawQuantity != null && rawQuantity > 0) ? rawQuantity : 0;
            float effort = (rawEffort != null && rawEffort > 0) ? rawEffort : 0;

            if (quantity > 0 && effort > 0) {
                PlanHeader planHeader = PlanMapper.INSTANCE.dtoToPlanHeader(productionPlanDTO, plan, productId, quantity, effort);

                Product product = productService.getProductById(productId);
                planHeader.setProduct(product);

                planHeaderRepository.save(planHeader);
            }
        }
    }

}