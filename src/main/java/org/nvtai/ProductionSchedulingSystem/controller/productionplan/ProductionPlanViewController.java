package org.nvtai.ProductionSchedulingSystem.controller.productionplan;

import org.nvtai.ProductionSchedulingSystem.dto.DailyProductionSummaryDTO;
import org.nvtai.ProductionSchedulingSystem.dto.ProductionPlanDetailDTO;
import org.nvtai.ProductionSchedulingSystem.dto.DailyProductionDTO;
import org.nvtai.ProductionSchedulingSystem.service.ProductionPlanDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProductionPlanViewController {

    @Autowired
    private ProductionPlanDetailService productionPlanDetailService;

    @GetMapping("/plan/view-data")
    @ResponseBody
    public Map<String, Object> showDetail(@RequestParam("plid") Integer id) {
        // Lấy chi tiết kế hoạch sản xuất từ service
        ProductionPlanDetailDTO planDetailsDTO = productionPlanDetailService.getProductionPlanDetail(id);

        // Tổng hợp dữ liệu sản xuất hàng ngày
        List<DailyProductionSummaryDTO> summarizedDailyProductions =
                productionPlanDetailService.summarizeDailyProduction(planDetailsDTO.getDailyProductions());

        // Sắp xếp theo ngày và ID sản phẩm
        summarizedDailyProductions.sort(Comparator.comparing(DailyProductionSummaryDTO::getDate)
                .thenComparing(DailyProductionSummaryDTO::getProductId));

        Map<String, Object> response = new HashMap<>();
        response.put("plan", planDetailsDTO.getPlan());
        response.put("department", planDetailsDTO.getDepartment());
        response.put("managerName", planDetailsDTO.getManagerName());
        response.put("productDetails", planDetailsDTO.getProductDetails());
        response.put("summarizedDailyProductions", summarizedDailyProductions);

        return response;  // Trả về dưới dạng JSON
    }

}