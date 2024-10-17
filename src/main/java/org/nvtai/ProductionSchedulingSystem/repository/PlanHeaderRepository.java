package org.nvtai.ProductionSchedulingSystem.repository;

import org.nvtai.ProductionSchedulingSystem.entity.PlanHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanHeaderRepository extends JpaRepository<PlanHeader, Integer> {
    PlanHeader findByPhid(int phid);

    List<PlanHeader> findAll();

    void deleteByPhid(int phid);

    boolean existsByPhid(int phid);

    PlanHeader save(PlanHeader planHeader);
}