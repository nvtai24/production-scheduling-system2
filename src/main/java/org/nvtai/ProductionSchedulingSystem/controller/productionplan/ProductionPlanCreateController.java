package org.nvtai.ProductionSchedulingSystem.controller.productionplan;

import org.nvtai.ProductionSchedulingSystem.dto.ProductionPlanDTO;
import org.nvtai.ProductionSchedulingSystem.entity.Department;
import org.nvtai.ProductionSchedulingSystem.entity.Plan;
import org.nvtai.ProductionSchedulingSystem.entity.Product;
import org.nvtai.ProductionSchedulingSystem.service.DepartmentService;
import org.nvtai.ProductionSchedulingSystem.service.PlanHeaderService;
import org.nvtai.ProductionSchedulingSystem.service.PlanService;
import org.nvtai.ProductionSchedulingSystem.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProductionPlanCreateController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PlanService planService;

    @Autowired
    private PlanHeaderService planHeaderService;

    @GetMapping("/plan/add-data")
    @ResponseBody
    public Map<String, Object> getPlanFormData() {
        List<Department> departments = departmentService.getDepartmentsByType("Production");
        List<Product> products = productService.getAllProducts();

        Map<String, Object> response = new HashMap<>();
        response.put("departments", departments);
        response.put("products", products);

        return response;
    }

    @PostMapping("/plan/add")
    @Transactional
    public String submit(@ModelAttribute ProductionPlanDTO productionPlanDTO) {
        Plan plan = planService.createPlan(productionPlanDTO);
        planHeaderService.createPlanHeader(productionPlanDTO, plan);
        return "redirect:/plan";
    }
}