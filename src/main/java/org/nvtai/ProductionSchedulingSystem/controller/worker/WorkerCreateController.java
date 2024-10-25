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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WorkerCreateController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SalaryService salaryService;

    @GetMapping(value = "/worker/add-data")
    @ResponseBody
    public Map<String, Object> getAddWorkerData() {
        Map<String, Object> data = new HashMap<>();

        List<Department> departments = departmentService.getDepartmentsByType("Production");
        List<Salary> salaries = salaryService.listSalaries();

        data.put("departments", departments);
        data.put("salaries", salaries);

        return data;
    }


    @PostMapping(value = "/worker/add")
    public String submit(@ModelAttribute("worker") EmployeeDTO worker) {
        employeeService.save(worker);
        return "redirect:/worker"; // Redirect back to the worker list page
    }
}