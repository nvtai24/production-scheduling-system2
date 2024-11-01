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

    public List<Plan> getPlansByDepartmentId(Integer did) {
        return planRepository.findByDepartmentDidAndIsdeletedFalse(did);
    }

    public List<Plan> getAvailablePlans() {
        return planRepository.findByIsdeletedFalse();
    }

    public Plan get(Integer id) {
        return planRepository.findById(id).orElse(null);
    }

    public void delete(Integer id) {
        planRepository.softDelete(id);
    }

    @Autowired
    private DepartmentService departmentService;


    public Plan createPlan(ProductionPlanDTO productionPlanDTO) {
        Plan plan = new Plan();
        plan.setPlname(productionPlanDTO.getPlanname());
        plan.setStartdate(productionPlanDTO.getStartdate());
        plan.setEnddate(productionPlanDTO.getEnddate());

        Department department = departmentService.getDepartmentById(productionPlanDTO.getDepartmentId());

        plan.setDepartment(department);

        return planRepository.save(plan);
    }

    public void updatePlan(ProductionPlanDTO productionPlanDTO, Integer plid) {
        Plan existedPlan = planRepository.findByPlid(plid);

        existedPlan.setPlname(productionPlanDTO.getPlanname());
        existedPlan.setStartdate(productionPlanDTO.getStartdate());
        existedPlan.setEnddate(productionPlanDTO.getEnddate());


        Department newDep = departmentService.getDepartmentById(productionPlanDTO.getDepartmentId());

        existedPlan.setDepartment(newDep);
        existedPlan.setNote(productionPlanDTO.getNote());

        planRepository.save(existedPlan);
    }

    public List<Plan> getCompletedPlans() {
        return planRepository.findByIscompletedTrue();
    }


}