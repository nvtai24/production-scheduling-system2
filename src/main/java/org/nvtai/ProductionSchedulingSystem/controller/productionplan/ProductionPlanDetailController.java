package org.nvtai.ProductionSchedulingSystem.controller.productionplan;

import org.nvtai.ProductionSchedulingSystem.dto.DailyProductionDTO;
import org.nvtai.ProductionSchedulingSystem.dto.DailyProductionSummaryDTO;
import org.nvtai.ProductionSchedulingSystem.dto.ProductionPlanDetailDTO;
import org.nvtai.ProductionSchedulingSystem.service.ProductionPlanDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;


@Controller
public class ProductionPlanDetailController {

    @Autowired
    private ProductionPlanDetailService productionPlanDetailService;

    @GetMapping(value = "/plan/detail")
    public String showDetail(@RequestParam("plid") Integer id, Model model) {
        ProductionPlanDetailDTO planDetailsDTO = productionPlanDetailService.getProductionPlanDetail(id);

        // Sắp xếp theo ngày và ca làm việc
        planDetailsDTO.getDailyProductions().sort(
                Comparator.comparing(DailyProductionDTO::getDate)
                        .thenComparing(DailyProductionDTO::getShift)
                        .thenComparing(DailyProductionDTO::getProductId)
        );

        model.addAttribute("planDetails", planDetailsDTO);
        return "/plan/details";
    }

}