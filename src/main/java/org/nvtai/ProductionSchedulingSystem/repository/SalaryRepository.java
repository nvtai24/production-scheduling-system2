package org.nvtai.ProductionSchedulingSystem.repository;

import org.nvtai.ProductionSchedulingSystem.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {
    Salary findBySid(int sid);

    List<Salary> findAll();

    void deleteBySid(int sid);

    boolean existsBySid(int sid);

    Salary save(Salary salary);
}