package org.nvtai.ProductionSchedulingSystem.repository;

import org.nvtai.ProductionSchedulingSystem.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {
    List<Plan> findByPlnameContaining(String plname);

    List<Plan> findByIsdeletedFalse();

    Plan findByPlid(int plid);

    void deleteByPlid(int plid);

    default void softDelete(int plid) {
        Plan plan = findByPlid(plid);
        plan.setIsdeleted(true);
        save(plan);
    }

    boolean existsByPlid(int plid);

    Plan save(Plan plan);
    
    List<Plan> findByIscompletedTrue();
}