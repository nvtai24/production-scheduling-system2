package org.nvtai.ProductionSchedulingSystem.controller.dailyproductionplan;

import org.nvtai.ProductionSchedulingSystem.entity.Plan;
import org.nvtai.ProductionSchedulingSystem.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DailyProductionShowController {

    @Autowired
    private PlanService planService;

    @GetMapping("/dailyproduction")
    public String showDailyProduction(Model model) {

        List<Plan> plans = planService.getAvailablePlans();

        model.addAttribute("plans", plans);

        return "/dailyproductionplan/view";
    } // OK

    @GetMapping("/calendar")
    public String showCalendar(@RequestParam("plid") Integer planId, Model model) {
        Plan selectedPlan = planService.get(planId); // Lấy kế hoạch sản xuất dựa vào ID
        model.addAttribute("selectedPlan", selectedPlan); // Truyền kế hoạch vào model
        return "/dailyproductionplan/calendar"; // Hiển thị trang lịch
    }


}