package org.nvtai.ProductionSchedulingSystem.repository;

import org.nvtai.ProductionSchedulingSystem.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByDname(String dname);

    Department findByDid(int did);

    List<Department> findAll();

    List<Department> findByType(String type);

    void deleteByDid(int did);

    void deleteByDname(String dname);

    boolean existsByDname(String dname);

    boolean existsByDid(int did);

    Department save(Department department);
}