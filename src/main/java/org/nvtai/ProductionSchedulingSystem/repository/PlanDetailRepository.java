package org.nvtai.ProductionSchedulingSystem.repository;

import org.nvtai.ProductionSchedulingSystem.entity.Plan;
import org.nvtai.ProductionSchedulingSystem.entity.PlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface PlanDetailRepository extends JpaRepository<PlanDetail, Integer> {

    List<PlanDetail> findByPlanHeaderPhid(Integer phid);

    List<PlanDetail> findByPlanHeaderPhidAndDate(Integer phid, Date date);

    PlanDetail findByPlanHeaderPhidAndShiftSidAndDate(Integer phid, Integer sid, Date date);



}