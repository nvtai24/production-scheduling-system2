package org.nvtai.ProductionSchedulingSystem.controller.productionplan;

import org.nvtai.ProductionSchedulingSystem.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductionPlanDeleteController {

    @Autowired
    private PlanService planService;

    @PostMapping(value = "/plan/remove")
    public String removeProductionPlan(@RequestParam("plid") int id) {
        planService.delete(id);

        return "redirect:/plan";
    }
}