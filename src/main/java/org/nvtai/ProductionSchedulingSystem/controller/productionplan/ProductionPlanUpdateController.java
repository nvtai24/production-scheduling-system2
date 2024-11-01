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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProductionPlanUpdateController {

    @Autowired
    private ProductionPlanDetailService productionPlanDetailService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ProductService productService;

    @GetMapping("/plan/edit-data")
    @ResponseBody
    public Map<String, Object> getEditPlanData(@RequestParam("plid") Integer id) {
        ProductionPlanDetailDTO planDetailsDTO = productionPlanDetailService.getProductionPlanDetail(id);
        List<Department> departments = departmentService.getDepartmentsByType("Production");
        List<Product> products = productService.getAllProducts();

        Map<String, Object> response = new HashMap<>();
        response.put("plan", planDetailsDTO.getPlan());
        response.put("departments", departments);
        response.put("products", products);
        response.put("planDetails", planDetailsDTO);

        planDetailsDTO.getPlan();

        return response;  // Trả về dưới dạng JSON
    }

    @Autowired
    private PlanService planService;

    @Autowired
    private PlanHeaderService planHeaderService;

    @PostMapping("/plan/edit")
    @Transactional
    public String submit(@ModelAttribute ProductionPlanDTO productionPlanDTO, @RequestParam("plid") Integer plid) {

        System.out.println(productionPlanDTO);

        planService.updatePlan(productionPlanDTO, plid);

        planHeaderService.updatePlanHeader(productionPlanDTO, plid);

//        return "redirect:/plan/detail?plid=" + plid;
        return "redirect:/plan";
    }
}