package org.nvtai.ProductionSchedulingSystem.repository;

import org.nvtai.ProductionSchedulingSystem.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    Attendance save(Attendance attendance);

    List<Attendance> findByWorkAssignmentWaid(Integer waid);
}