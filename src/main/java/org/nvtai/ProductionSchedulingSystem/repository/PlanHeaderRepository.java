package org.nvtai.ProductionSchedulingSystem.repository;

import org.nvtai.ProductionSchedulingSystem.entity.PlanDetail;
import org.nvtai.ProductionSchedulingSystem.entity.PlanHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanHeaderRepository extends JpaRepository<PlanHeader, Integer> {
    PlanHeader findByPhid(int phid);

    List<PlanHeader> findByPlanPlid(int plid);


    @Query("SELECT pd FROM PlanDetail pd WHERE pd.planHeader.phid = :phid AND MONTH(pd.date) = :month AND YEAR(pd.date) = :year")
    List<PlanDetail> findByPlanHeaderIdAndMonthAndYear(@Param("phid") Integer phid,
                                                       @Param("month") int month,
                                                       @Param("year") int year);

    List<PlanHeader> findAll();

    void deleteByPhid(int phid);

    boolean existsByPhid(int phid);

    PlanHeader save(PlanHeader planHeader);

    PlanHeader findByPlanPlidAndProductPid(int plid, int pid);
}