package org.nvtai.ProductionSchedulingSystem.service;

import org.nvtai.ProductionSchedulingSystem.dto.DailyProductionDTO;
import org.nvtai.ProductionSchedulingSystem.dto.DailyProductionSummaryDTO;
import org.nvtai.ProductionSchedulingSystem.dto.ProductDetailDTO;
import org.nvtai.ProductionSchedulingSystem.dto.ProductionPlanDetailDTO;
import org.nvtai.ProductionSchedulingSystem.entity.*;
import org.nvtai.ProductionSchedulingSystem.repository.PlanDetailRepository;
import org.nvtai.ProductionSchedulingSystem.repository.PlanHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                dailyProductionDTO.setProductId(planHeader.getProduct().getPid());
                dailyProductionDTO.setProductName(planHeader.getProduct().getPname());
                dailyProductionDTO.setShift(planDetail.getShift().getSname());
                dailyProductionDTO.setQuantity(planDetail.getQuantity());
                dailyProductionDTO.setNote(planDetail.getNote());
                dailyProductions.add(dailyProductionDTO);
            }
        }

        planDetailsDTO.setDailyProductions(dailyProductions);

        return planDetailsDTO;
    }


    public List<DailyProductionSummaryDTO> summarizeDailyProduction(List<DailyProductionDTO> dailyProductions) {
        // Sử dụng Map để nhóm các sản phẩm theo ngày và sản phẩm, sau đó tính tổng số lượng
        Map<String, DailyProductionSummaryDTO> summaryMap = new HashMap<>();

        for (DailyProductionDTO dailyProduction : dailyProductions) {
            String key = dailyProduction.getDate() + "_" + dailyProduction.getProductId(); // Tạo key để nhóm theo ngày và sản phẩm

            // Nếu đã tồn tại trong Map, cộng thêm số lượng
            if (summaryMap.containsKey(key)) {
                DailyProductionSummaryDTO summary = summaryMap.get(key);
                summary.setTotalQuantity(summary.getTotalQuantity() + dailyProduction.getQuantity());
            } else {
                // Nếu chưa tồn tại, tạo mới
                DailyProductionSummaryDTO newSummary = new DailyProductionSummaryDTO();
                newSummary.setDate(dailyProduction.getDate());
                newSummary.setProductId(dailyProduction.getProductId());
                newSummary.setProductName(dailyProduction.getProductName());
                newSummary.setTotalQuantity(dailyProduction.getQuantity());
                summaryMap.put(key, newSummary);
            }
        }

        // Trả về danh sách đã tổng hợp
        return new ArrayList<>(summaryMap.values());
    }






}