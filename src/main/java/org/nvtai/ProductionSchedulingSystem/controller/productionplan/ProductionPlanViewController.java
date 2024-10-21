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

import java.util.Comparator;
import java.util.List;

@Controller
public class ProductionPlanViewController {

    @Autowired
    private ProductionPlanDetailService productionPlanDetailService;

    @GetMapping(value = "/productionplan/view")
    public String showDetail(@RequestParam("plid") Integer id, Model model) {
        // Lấy dữ liệu chi tiết kế hoạch sản xuất
        ProductionPlanDetailDTO planDetailsDTO = productionPlanDetailService.getProductionPlanDetail(id);

        // Tổng hợp dữ liệu theo ngày và sản phẩm
        List<DailyProductionSummaryDTO> summarizedDailyProductions =
                productionPlanDetailService.summarizeDailyProduction(planDetailsDTO.getDailyProductions());

        summarizedDailyProductions.sort(Comparator.comparing(DailyProductionSummaryDTO::getDate)
                .thenComparing(DailyProductionSummaryDTO::getProductId));

        // Thêm dữ liệu vào model để hiển thị trên Thymeleaf
        model.addAttribute("planDetails", planDetailsDTO);
        model.addAttribute("summarizedDailyProductions", summarizedDailyProductions);

        return "productionplan/view";
    }
}