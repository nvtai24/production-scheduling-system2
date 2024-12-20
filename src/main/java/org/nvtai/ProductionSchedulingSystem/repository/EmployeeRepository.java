package org.nvtai.ProductionSchedulingSystem.repository;

import org.nvtai.ProductionSchedulingSystem.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEname(String ename);

    Employee findByEid(int eid);

    List<Employee> findByDepartmentDidAndIsmanager(int did, boolean ismanager);

    void deleteByEid(int eid);

    void deleteByEname(String ename);

    boolean existsByEname(String ename);

    boolean existsByEid(int eid);

    Employee save(Employee employee);

    List<Employee> findByIsmanagerAndIsdeleted(boolean ismanager, boolean isdeleted);

    default void softDelete(int eid) {
        Employee employee = findByEid(eid);
        employee.setIsdeleted(true);
        save(employee);
    };

    List<Employee> findByDepartmentDidAndIsmanagerAndIsdeleted(Integer did, boolean b, boolean b1);

}