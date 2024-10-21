package org.nvtai.ProductionSchedulingSystem.service;

import org.nvtai.ProductionSchedulingSystem.entity.Salary;
import org.nvtai.ProductionSchedulingSystem.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaryService {

    @Autowired
    private SalaryRepository salaryRepository;

    public List<Salary> listSalaries() {
        return salaryRepository.findAll();
    }

    public Salary get(int sid) {
        return salaryRepository.findBySid(sid);
    }
}