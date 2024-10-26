package org.nvtai.ProductionSchedulingSystem.repository;

import org.nvtai.ProductionSchedulingSystem.entity.PlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface PlanDetailRepository extends JpaRepository<PlanDetail, Integer> {

    List<PlanDetail> findByPlanHeaderPhid(Integer phid);

    PlanDetail findByPlanHeaderPhidAndShiftSidAndDate(int phid, int sid, Date date);

}