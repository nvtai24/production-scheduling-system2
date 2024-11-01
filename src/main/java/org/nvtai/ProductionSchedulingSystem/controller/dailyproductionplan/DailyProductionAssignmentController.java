package org.nvtai.ProductionSchedulingSystem.controller.dailyproductionplan;

import org.nvtai.ProductionSchedulingSystem.entity.*;
import org.nvtai.ProductionSchedulingSystem.repository.WorkAssignmentRepository;
import org.nvtai.ProductionSchedulingSystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
            @RequestParam("plid") Integer plid, @RequestParam("date") String date, Model model) {
        Plan selectedPlan = planService.get(plid);
        List<Employee> employees =
                employeeService.getWorkersByDid(selectedPlan.getDepartment().getDid());
        List<Product> productsPlanned = planHeaderService.getPlanHeadersByPlanId(plid).stream()
                .map(PlanHeader::getProduct)
                .collect(Collectors.toList());
        List<Shift> shifts = shiftService.getAllShifts();

        List<WorkAssignment> assignments = workAssignmentService.getAssignmentsByPlanIdAndDate(plid, date);
        Map<String, WorkAssignment> assignmentMap = new HashMap<>();

        for (WorkAssignment assignment : assignments) {
            String key = assignment.getPlanDetail().getShift().getSid() + "_"
                    + assignment.getPlanDetail().getPlanHeader().getProduct().getPid()
                    + "_" + assignment.getEid();
            assignmentMap.put(key, assignment);
        }

        model.addAttribute("employees", employees);
        model.addAttribute("productsPlanned", productsPlanned);
        model.addAttribute("shifts", shifts);
        model.addAttribute("assignmentMap", assignmentMap);
        model.addAttribute("plid", plid);
        model.addAttribute("date", date);

        return "/dailyproductionplan/assignment";
    }

    @Autowired
    private WorkAssignmentService workAssignmentService;

    @PostMapping("/dailyproduction/assignments")
    public String saveWorkAssignments(
            @RequestParam Integer plid, @RequestParam String date, @RequestParam Map<String, String> allParams) {
        workAssignmentService.saveWorkAssignments(plid, date, allParams);
        return "redirect:/showdailydetail?plid=" + plid + "&date="
                + date; // Thay bằng URL mà bạn muốn chuyển hướng sau khi lưu
    }
}