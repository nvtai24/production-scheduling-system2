package org.nvtai.ProductionSchedulingSystem.repository;

import org.nvtai.ProductionSchedulingSystem.entity.WorkAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkAssignmentRepository extends JpaRepository<WorkAssignment, Integer> {

    WorkAssignment save(WorkAssignment workAssignment);

    List<WorkAssignment> findByPlanDetailPdid(Integer pdid);
}