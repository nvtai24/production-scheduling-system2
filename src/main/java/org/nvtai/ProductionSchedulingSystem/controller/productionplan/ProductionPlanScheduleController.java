package org.nvtai.ProductionSchedulingSystem.controller.productionplan;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductionPlanScheduleController {

    @GetMapping("/schedule")
    public String show() {
        return "plan/schedule";
    }

}