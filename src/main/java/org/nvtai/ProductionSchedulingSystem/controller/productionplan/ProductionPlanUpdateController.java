package org.nvtai.ProductionSchedulingSystem.controller.productionplan;

import org.nvtai.ProductionSchedulingSystem.dto.ProductionPlanDTO;
import org.nvtai.ProductionSchedulingSystem.dto.ProductionPlanDetailDTO;
import org.nvtai.ProductionSchedulingSystem.entity.Department;
import org.nvtai.ProductionSchedulingSystem.entity.Product;
import org.nvtai.ProductionSchedulingSystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductionPlanUpdateController {

    @Autowired
    private ProductionPlanDetailService productionPlanDetailService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ProductService productService;

    @GetMapping("/productionplan/edit")
    public String showForm(@RequestParam("plid") Integer id, Model model) {
        ProductionPlanDetailDTO planDetailsDTO = productionPlanDetailService.getProductionPlanDetail(id);

        List<Department> departments = departmentService.getDepartmentsByType("Production");
        List<Product> products = productService.getAllProducts();

        model.addAttribute("departments", departments);
        model.addAttribute("products", products);
        model.addAttribute("planDetails", planDetailsDTO);

        return "productionplan/edit";
    }

    @Autowired
    private PlanService planService;

    @Autowired
    private PlanHeaderService planHeaderService;

    @PostMapping("/productionplan/edit")
    @Transactional
    public String submit(@ModelAttribute ProductionPlanDTO productionPlanDTO, @RequestParam("plid") Integer plid) {

        System.out.println(productionPlanDTO);

        planService.updatePlan(productionPlanDTO, plid);

        planHeaderService.updatePlanHeader(productionPlanDTO, plid);

        return "redirect:/productionplan/detail?plid=" + plid;
    }
}