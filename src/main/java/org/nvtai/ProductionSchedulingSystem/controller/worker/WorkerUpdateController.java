package org.nvtai.ProductionSchedulingSystem.controller.worker;

import org.nvtai.ProductionSchedulingSystem.dto.EmployeeDTO;
import org.nvtai.ProductionSchedulingSystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WorkerUpdateController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping(value = "/worker/edit")
    public String updateWorker(@ModelAttribute("worker") EmployeeDTO worker) {
        // Call service to update worker details

        System.out.println(worker);
        employeeService.updateWorker(worker);
        return "redirect:/worker/list"; // Redirect back to the worker list page
    }
}