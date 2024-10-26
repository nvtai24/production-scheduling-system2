package org.nvtai.ProductionSchedulingSystem.controller.productionplan;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductionPlanAttendanceController {

    @GetMapping("/attendance")
    public String showAttendance() {
        return "plan/calendar";
    }
}