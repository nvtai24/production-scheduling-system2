package org.nvtai.ProductionSchedulingSystem.controller.worker;

import org.nvtai.ProductionSchedulingSystem.dto.EmployeeDTO;
import org.nvtai.ProductionSchedulingSystem.entity.Department;
import org.nvtai.ProductionSchedulingSystem.entity.Salary;
import org.nvtai.ProductionSchedulingSystem.service.DepartmentService;
import org.nvtai.ProductionSchedulingSystem.service.EmployeeService;
import org.nvtai.ProductionSchedulingSystem.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class WorkerCreateController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SalaryService salaryService;

    @GetMapping(value = "/worker/add")
    public String showForm(Model model) {

        List<Department> departments = departmentService.getDepartmentsByType("Production");
        List<Salary> salaries = salaryService.listSalaries();

        model.addAttribute("departments", departments);
        model.addAttribute("salaries", salaries);

        return "worker/add";
    }

    @PostMapping(value = "/worker/add")
    public String submit(@ModelAttribute("worker") EmployeeDTO worker) {
        employeeService.save(worker);
        return "redirect:/worker/list"; // Redirect back to the worker list page
    }
}