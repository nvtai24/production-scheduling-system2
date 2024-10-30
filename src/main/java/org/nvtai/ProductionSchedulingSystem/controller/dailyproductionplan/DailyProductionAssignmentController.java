package org.nvtai.ProductionSchedulingSystem.controller.dailyproductionplan;

import org.nvtai.ProductionSchedulingSystem.entity.*;
import org.nvtai.ProductionSchedulingSystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class DailyProductionAssignmentController {

    @Autowired
    private EmployeeService employeeService; // Service để lấy danh sách nhân viên

    @Autowired
    private ProductService productService; // Service để lấy danh sách sản phẩm

    @Autowired
    private ShiftService shiftService; // Service để lấy danh sách ca làm việc

    @Autowired
    private PlanService planService; // Service để lấy danh sách kế hoạch sản xuất

    @Autowired
    private PlanHeaderService planHeaderService; // Service để lấy danh sách kế hoạch sản xuất

    @GetMapping("/dailyproduction/assignment")
    public String showDailyProductionAssignment(
            @RequestParam("planId") Integer planId, @RequestParam("date") String date, Model model) {
        Plan selectedPlan = planService.get(planId); // Lấy kế hoạch sản xuất dựa vào ID

        List<Employee> employees = employeeService.getWorkersByDid(
                selectedPlan.getDepartment().getDid()); // Lấy danh sách nhân viên của phòng ban

        List<Product> products = productService.getAllProducts(); // Lấy danh sách sản phẩm

        List<Product> productsPlanned = new ArrayList<>();
        List<PlanHeader> headers = planHeaderService.getPlanHeadersByPlanId(planId);

        model.addAttribute("productsPlanned", productsPlanned);

        model.addAttribute("planId", planId);
        model.addAttribute("date", date);

        for (PlanHeader header : headers) {
            productsPlanned.add(header.getProduct());
        }

        List<Shift> shifts = shiftService.getAllShifts();

        model.addAttribute("employees", employees);
        model.addAttribute("products", products);
        model.addAttribute("shifts", shifts);

        return "/dailyproductionplan/assignment";
    }

    @Autowired
    private WorkAssignmentService workAssignmentService;

    @PostMapping("/dailyproduction/assignments")
    public String saveWorkAssignments(
            @RequestParam("planId") Integer planId,
            @RequestParam("date") String date,
            @RequestParam Map<String, String> allParams) {

        workAssignmentService.saveWorkAssignments(planId, date, allParams);

        return "redirect:/showdailydetail?plid=" + planId + "&date=" + date;
    }
}