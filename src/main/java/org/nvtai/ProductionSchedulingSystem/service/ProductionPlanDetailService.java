package org.nvtai.ProductionSchedulingSystem.service;

import org.nvtai.ProductionSchedulingSystem.dto.DailyProductionDTO;
import org.nvtai.ProductionSchedulingSystem.dto.ProductDetailDTO;
import org.nvtai.ProductionSchedulingSystem.dto.ProductionPlanDetailDTO;
import org.nvtai.ProductionSchedulingSystem.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductionPlanDetailService {
    @Autowired
    private PlanService planService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PlanHeaderService planHeaderService;

    @Autowired
    private PlanDetailService planDetailService;

    public ProductionPlanDetailDTO getProductionPlanDetail(Integer id) {
        ProductionPlanDetailDTO planDetailsDTO = new ProductionPlanDetailDTO();

        Plan plan = planService.get(id);
        planDetailsDTO.setPlan(plan);

        Department department = departmentService.getDepartmentById(plan.getDepartment().getDid());
        planDetailsDTO.setDepartment(department);

        Employee manager = employeeService.getManagerByDid(department.getDid());
        planDetailsDTO.setManagerName(manager.getEname());

        List<ProductDetailDTO> productDetails = new ArrayList<>();

        List<PlanHeader> planHeaders = planHeaderService.getPlanHeadersByPlanId(plan.getPlid());

        for (PlanHeader planHeader : planHeaders) {
            ProductDetailDTO productDetailDTO = new ProductDetailDTO();
            productDetailDTO.setProductId(planHeader.getProduct().getPid());
            productDetailDTO.setProductName(planHeader.getProduct().getPname());
            productDetailDTO.setQuantity(planHeader.getQuantity());
            productDetailDTO.setEffort(planHeader.getEstimatedeffort());
            productDetails.add(productDetailDTO);
        }

        planDetailsDTO.setProductDetails(productDetails);

        List<DailyProductionDTO> dailyProductions = new ArrayList<>();

        for (PlanHeader planHeader : planHeaders) {
            List<PlanDetail> dailyProduction = planDetailService.getPlanDetailsByPlanHeaderId(planHeader.getPhid());

            for (PlanDetail planDetail : dailyProduction) {
                DailyProductionDTO dailyProductionDTO = new DailyProductionDTO();
                dailyProductionDTO.setDate(planDetail.getDate());
                dailyProductionDTO.setShift(planDetail.getShift().getSname());
                dailyProductionDTO.setQuantity(planDetail.getQuantity());
                dailyProductionDTO.setNote(planDetail.getNote());
                dailyProductions.add(dailyProductionDTO);
            }
        }

        planDetailsDTO.setDailyProductions(dailyProductions);

        return planDetailsDTO;
    }



}