package org.nvtai.ProductionSchedulingSystem.service;

import org.nvtai.ProductionSchedulingSystem.dto.ProductionPlanDTO;
import org.nvtai.ProductionSchedulingSystem.entity.Department;
import org.nvtai.ProductionSchedulingSystem.entity.Plan;
import org.nvtai.ProductionSchedulingSystem.mapper.PlanMapper;
import org.nvtai.ProductionSchedulingSystem.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }


    @Autowired
    private DepartmentService departmentService;


    public Plan createPlan(ProductionPlanDTO productionPlanDTO) {
        Plan plan = PlanMapper.INSTANCE.dtoToPlan(productionPlanDTO);

        Department department = departmentService.getDepartmentById(productionPlanDTO.getDepartmentId());

        plan.setDepartment(department);

        return planRepository.save(plan);
    }


}