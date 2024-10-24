package org.nvtai.ProductionSchedulingSystem.controller.worker;

import org.nvtai.ProductionSchedulingSystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class WorkerDeleteController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping(value = "/worker/remove")
    public String deleteWorker(@RequestParam("eid") int eid) {
        employeeService.delete(eid);

        return "redirect:/worker";
    }

}