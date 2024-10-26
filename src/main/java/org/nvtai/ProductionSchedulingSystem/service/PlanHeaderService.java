package org.nvtai.ProductionSchedulingSystem.service;

import org.nvtai.ProductionSchedulingSystem.dto.ProductionPlanDTO;
import org.nvtai.ProductionSchedulingSystem.entity.Plan;
import org.nvtai.ProductionSchedulingSystem.entity.PlanHeader;
import org.nvtai.ProductionSchedulingSystem.entity.Product;
import org.nvtai.ProductionSchedulingSystem.repository.PlanHeaderRepository;
import org.nvtai.ProductionSchedulingSystem.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PlanHeaderService {

    @Autowired
    private PlanHeaderRepository planHeaderRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private ProductService productService;

    public List<PlanHeader> getPlanHeadersByPlanId(Integer planId) {
        return planHeaderRepository.findByPlanPlid(planId);
    }

    public void createPlanHeader(ProductionPlanDTO productionPlanDTO, Plan plan) {
        for (Integer productId : productionPlanDTO.getQuantities().keySet()) {
            Integer quantity = getValidQuantity(productionPlanDTO, productId);
            Float effort = getValidEffort(productionPlanDTO, productId);

            if (quantity > 0 && effort > 0) {
                PlanHeader newPlanHeader = createNewPlanHeader(plan, productId, quantity, effort);
                planHeaderRepository.save(newPlanHeader);
            }
        }
    }

    public void updatePlanHeader(ProductionPlanDTO productionPlanDTO, Integer plid) {
        List<PlanHeader> planHeaders = planHeaderRepository.findByPlanPlid(plid);
        Plan plan = planRepository.findByPlid(plid);

        Set<Integer> updatedProductIds = new HashSet<>();

        // Cập nhật các sản phẩm đã tồn tại trong PlanHeader
        for (PlanHeader planHeader : planHeaders) {
            Integer productId = planHeader.getProduct().getPid();
            updatedProductIds.add(productId);

            Integer quantity = getValidQuantity(productionPlanDTO, productId);
            Float effort = getValidEffort(productionPlanDTO, productId);

            if (quantity > 0 && effort > 0) {
                updateExistingPlanHeader(planHeader, quantity, effort);
            } else {
                planHeaderRepository.delete(planHeader);
            }
        }

        // Thêm các sản phẩm mới (nếu có)
        createNewPlanHeaders(productionPlanDTO, plan, updatedProductIds);
    }

    private Integer getValidQuantity(ProductionPlanDTO productionPlanDTO, Integer productId) {
        Integer rawQuantity = productionPlanDTO.getQuantities().get(productId);
        return (rawQuantity != null && rawQuantity > 0) ? rawQuantity : 0;
    }

    private Float getValidEffort(ProductionPlanDTO productionPlanDTO, Integer productId) {
        Float rawEffort = productionPlanDTO.getEfforts().get(productId);
        return (rawEffort != null && rawEffort > 0) ? rawEffort : 0;
    }

    private void updateExistingPlanHeader(PlanHeader planHeader, Integer quantity, Float effort) {
        planHeader.setQuantity(quantity);
        planHeader.setEstimatedeffort(effort);
        planHeaderRepository.save(planHeader);  // Cập nhật PlanHeader hiện có
    }

    private void createNewPlanHeaders(ProductionPlanDTO productionPlanDTO, Plan plan, Set<Integer> updatedProductIds) {
        for (Integer productId : productionPlanDTO.getQuantities().keySet()) {
            if (!updatedProductIds.contains(productId)) {
                Integer quantity = getValidQuantity(productionPlanDTO, productId);
                Float effort = getValidEffort(productionPlanDTO, productId);

                if (quantity > 0 && effort > 0) {
                    PlanHeader newPlanHeader = createNewPlanHeader(plan, productId, quantity, effort);
                    planHeaderRepository.save(newPlanHeader);
                }
            }
        }
    }

    private PlanHeader createNewPlanHeader(Plan plan, Integer productId, Integer quantity, Float effort) {
        PlanHeader newPlanHeader = new PlanHeader();
        newPlanHeader.setPlan(plan);
        newPlanHeader.setQuantity(quantity);
        newPlanHeader.setEstimatedeffort(effort);

        Product product = productService.getProductById(productId);
        newPlanHeader.setProduct(product);

        return newPlanHeader;
    }

    public PlanHeader getPlanHeaderIdByPlanIdAndProductId(Integer planId, Integer productId) {
        return planHeaderRepository.findByPlanPlidAndProductPid(planId, productId);
    }


}