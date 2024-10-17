package org.nvtai.ProductionSchedulingSystem.controller.productionplan;

import org.nvtai.ProductionSchedulingSystem.entity.Plan;
import org.nvtai.ProductionSchedulingSystem.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductionPlanListController {
    @Autowired
    private PlanService planService;

    @GetMapping("/productionplan/list")
    public String listProductionPlans(Model model) {
        // Lấy danh sách các plans từ service
        List<Plan> plans = planService.getAllPlans();

        // Đưa danh sách plans vào model để hiển thị trong view
        model.addAttribute("plans", plans);

        return "productionplan/list"; // Trả về trang HTML hiển thị danh sách plans
    }

}