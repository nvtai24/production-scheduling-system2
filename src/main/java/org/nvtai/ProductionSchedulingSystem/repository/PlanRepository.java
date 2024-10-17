package org.nvtai.ProductionSchedulingSystem.repository;

import org.nvtai.ProductionSchedulingSystem.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {
    List<Plan> findByPlnameContaining(String plname);

    Plan findByPlid(int plid);

    void deleteByPlid(int plid);

    boolean existsByPlid(int plid);

    Plan save(Plan plan);
}