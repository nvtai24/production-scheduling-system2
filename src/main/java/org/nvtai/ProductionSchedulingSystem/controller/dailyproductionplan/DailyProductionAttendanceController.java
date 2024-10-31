package org.nvtai.ProductionSchedulingSystem.controller.dailyproductionplan;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DailyProductionAttendanceController {

    @GetMapping("/dailyproduction/attendance")
    public String showDailyProductionAttendance() {
        return "dailyproductionplan/attendance";
    }
}