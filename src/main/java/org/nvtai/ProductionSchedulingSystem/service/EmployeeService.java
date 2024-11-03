package org.nvtai.ProductionSchedulingSystem.service;

import org.nvtai.ProductionSchedulingSystem.dto.EmployeeDTO;
import org.nvtai.ProductionSchedulingSystem.entity.Department;
import org.nvtai.ProductionSchedulingSystem.entity.Employee;
import org.nvtai.ProductionSchedulingSystem.entity.Salary;
import org.nvtai.ProductionSchedulingSystem.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SalaryService salaryService;

    public EmployeeDTO getEmployeeByEid(int eid) {

        Employee employee = employeeRepository.findByEid(eid);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEid(employee.getEid());
        employeeDTO.setEname(employee.getEname());
        employeeDTO.setPhonenumber(employee.getPhoneNumber());
        employeeDTO.setAddress(employee.getAddress());
        employeeDTO.setDid(employee.getDepartment().getDid());
        employeeDTO.setSid(employee.getSalary().getSid());
        return employeeDTO;
    }

    public Employee getWorkerByEid(int eid) {
        return employeeRepository.findByEid(eid);
    }

    public Employee getManagerByDid(int did) {
        return employeeRepository.findByDepartmentDidAndIsmanager(did, true).get(0);
    }

    public List<Employee> listWorkers() {
        return employeeRepository.findByIsmanagerAndIsdeleted(false, false);
    }

    public void delete(int eid) {
        employeeRepository.softDelete(eid);
    }

    public void updateWorker(EmployeeDTO worker) {
        Employee oldWorker = employeeRepository.findByEid(worker.getEid());

        Department department = departmentService.getDepartmentById(worker.getDid());
        oldWorker.setDepartment(department);

        oldWorker.setPhoneNumber(worker.getPhonenumber());
        oldWorker.setAddress(worker.getAddress());

        Salary newSalary = salaryService.get(worker.getSid());
        oldWorker.setSalary(newSalary);

        employeeRepository.save(oldWorker);
    }

    public void save(EmployeeDTO worker) {
        Employee newWorker = new Employee();

        if (employeeRepository.findByEid(worker.getEid()) != null) {
            newWorker = employeeRepository.findByEid(worker.getEid());
        }

        Department department = departmentService.getDepartmentById(worker.getDid());
        newWorker.setDepartment(department);

        newWorker.setEname(worker.getEname());
        newWorker.setPhoneNumber(worker.getPhonenumber());
        newWorker.setAddress(worker.getAddress());

        Salary salary = salaryService.get(worker.getSid());
        newWorker.setSalary(salary);

        employeeRepository.save(newWorker);
    }

    public List<Employee> getWorkersByDid(Integer did) {
        return employeeRepository.findByDepartmentDidAndIsmanagerAndIsdeleted(did, false, false);
    }
}