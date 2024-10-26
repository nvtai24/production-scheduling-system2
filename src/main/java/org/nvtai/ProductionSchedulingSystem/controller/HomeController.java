package org.nvtai.ProductionSchedulingSystem.controller;

import org.nvtai.ProductionSchedulingSystem.dto.EmployeeDTO;
import org.nvtai.ProductionSchedulingSystem.entity.Plan;
import org.nvtai.ProductionSchedulingSystem.entity.User;
import org.nvtai.ProductionSchedulingSystem.service.EmployeeService;
import org.nvtai.ProductionSchedulingSystem.service.PlanService;
import org.nvtai.ProductionSchedulingSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PlanService planService;

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName(); // Get the username
            model.addAttribute("username", username);
        }

        User user = userService.getUserByUsername(principal.getName());
        EmployeeDTO employee = employeeService.getEmployeeByEid(user.getEmployee().getEid());

        List<Plan> plans = planService.getAvailablePlans();
        List<Plan> completedPlans = planService.getCompletedPlans();

        model.addAttribute("employee", employee);
        model.addAttribute("user", user);
        model.addAttribute("plans", plans);
        model.addAttribute("completedPlans", completedPlans);

        return "home";
    }
}