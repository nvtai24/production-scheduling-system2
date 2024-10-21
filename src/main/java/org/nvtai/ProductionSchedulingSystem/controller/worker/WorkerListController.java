package org.nvtai.ProductionSchedulingSystem.controller.worker;

import org.nvtai.ProductionSchedulingSystem.entity.Department;
import org.nvtai.ProductionSchedulingSystem.entity.Employee;
import org.nvtai.ProductionSchedulingSystem.entity.Salary;
import org.nvtai.ProductionSchedulingSystem.service.DepartmentService;
import org.nvtai.ProductionSchedulingSystem.service.EmployeeService;
import org.nvtai.ProductionSchedulingSystem.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WorkerListController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SalaryService salaryService;

    @GetMapping(value = "/worker/list")
    private String showList(Model model) {
        // Show list of workers

        List<Employee> workers = employeeService.listWorkers();

        List<Department> departments = departmentService.getDepartmentsByType("Production");

        List<Salary> salaries = salaryService.listSalaries();

        model.addAttribute("workers", workers);
        model.addAttribute("departments", departments);
        model.addAttribute("salaries", salaries);

        return "worker/list";
    }
}