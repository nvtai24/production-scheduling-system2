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

import java.util.List;

@Controller
public class ProductionPlanCreateController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ProductService productService;

    @GetMapping("/productionplan/create")
    public String showForm(Model model) {
        List<Department> departments = departmentService.getDepartmentsByType("Production");
        List<Product> products = productService.getAllProducts();

        // Truyền danh sách departments và products vào model
        model.addAttribute("departments", departments);
        model.addAttribute("products", products);

        return "productionplan/create"; // Trả về trang create.html
    }

    @Autowired
    private PlanService planService;

    @Autowired
    private PlanHeaderService planHeaderService;

    @PostMapping("/productionplan/create")
    @Transactional
    public String submitForm(@ModelAttribute ProductionPlanDTO productionPlanDTO, Model model) {
        Plan plan = planService.createPlan(productionPlanDTO);

        planHeaderService.createPlanHeader(productionPlanDTO, plan);

        return "redirect:/productionplan/list";
    }

}