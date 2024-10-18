package org.nvtai.ProductionSchedulingSystem.service;

import org.nvtai.ProductionSchedulingSystem.entity.PlanDetail;
import org.nvtai.ProductionSchedulingSystem.repository.PlanDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanDetailService {

    @Autowired
    private PlanDetailRepository planDetailRepository;

    public List<PlanDetail> getPlanDetailsByPlanHeaderId(Integer phid) {
        return planDetailRepository.findByPlanHeaderPhid(phid);
    }

}