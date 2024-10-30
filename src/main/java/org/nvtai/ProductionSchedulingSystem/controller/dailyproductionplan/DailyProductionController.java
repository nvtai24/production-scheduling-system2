package org.nvtai.ProductionSchedulingSystem.controller.dailyproductionplan;

import org.nvtai.ProductionSchedulingSystem.dto.DailyProductionDTO;
import org.nvtai.ProductionSchedulingSystem.dto.ProductionPlanDetailDTO;
import org.nvtai.ProductionSchedulingSystem.entity.Employee;
import org.nvtai.ProductionSchedulingSystem.entity.Plan;
import org.nvtai.ProductionSchedulingSystem.entity.PlanDetail;
import org.nvtai.ProductionSchedulingSystem.service.EmployeeService;
import org.nvtai.ProductionSchedulingSystem.service.PlanDetailService;
import org.nvtai.ProductionSchedulingSystem.service.PlanService;
import org.nvtai.ProductionSchedulingSystem.service.ProductionPlanDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.Comparator;
import java.util.List;

@Controller
public class DailyProductionController {

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

    @Autowired
    private ProductionPlanDetailService productionPlanDetailService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/showdailydetail")
    public String showDailyDetail(
            @RequestParam("plid") Integer planId, @RequestParam("date") String date, Model model) {

        model.addAttribute("date", date); // Truyền ngày vào model

        ProductionPlanDetailDTO planDetails =
                productionPlanDetailService.getProductionPlanDetail(planId, Date.valueOf(date));

        planDetails.getDailyProductions().sort(
                Comparator.comparing(DailyProductionDTO::getShift)
                        .thenComparing(DailyProductionDTO::getProductId)
        );

        List<Employee> workers = employeeService.getWorkersByDid(planDetails.getDepartment().getDid());

        model.addAttribute("planDetails", planDetails); // Truyền chi tiết sản xuất vào model
        model.addAttribute("workers", workers); // Truyền danh sách công nhân vào model
        return "/dailyproductionplan/dailydetail"; // Hiển thị trang chi tiết sản xuất
    }
}