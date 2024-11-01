package org.nvtai.ProductionSchedulingSystem.controller;

import org.nvtai.ProductionSchedulingSystem.dto.salaryreport.EmployeeSalaryReport;
import org.nvtai.ProductionSchedulingSystem.entity.*;
import org.nvtai.ProductionSchedulingSystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SalaryReportController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PlanService planService;

    @Autowired
    private PlanHeaderService planHeaderService;

    @Autowired
    private PlanDetailService planDetailService;

    @Autowired
    private WorkAssignmentService workAssignmentService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/salary")
    public String showSalaryReportPage(Model model) {
        List<Department> departments = departmentService.getDepartmentsByType("Production");
        model.addAttribute("departments", departments);
        return "salaryreport";
    }

    @PostMapping("/salary")
    public String exportSalaryReport(
            Model model,
            @RequestParam("month") Integer month,
            @RequestParam("year") Integer year,
            @RequestParam("did") Integer did) {

        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("did", did);

        List<Department> departments = departmentService.getDepartmentsByType("Production");
        model.addAttribute("departments", departments);

        List<Plan> plans = planService.getPlansByDepartmentId(did);
        List<PlanHeader> planHeaders = new ArrayList<>();
        for (Plan plan : plans) {
            planHeaders.addAll(planHeaderService.getPlanHeadersByPlanId(plan.getPlid()));
        }

        List<PlanDetail> planDetails = new ArrayList<>();
        for (PlanHeader planHeader : planHeaders) {
            planDetails.addAll(
                    planDetailService.getPlanDetailsByPlanHeaderIdAndMonthAndYear(planHeader.getPhid(), month, year));
        }

        List<WorkAssignment> workAssignments = new ArrayList<>();
        for (PlanDetail planDetail : planDetails) {
            workAssignments.addAll(workAssignmentService.getWorkAssignmentsByPlanDetailId(planDetail.getPdid()));
        }

        List<Attendance> attendances = new ArrayList<>();
        for (WorkAssignment workAssignment : workAssignments) {
            attendances.addAll(attendanceService.findByWorkAssignmentWaid(workAssignment.getWaid()));
        }

        Map<Integer, EmployeeSalaryReport> reportMap = new HashMap<>();

        for (Attendance attendance : attendances) {
            int eid = attendance.getWorkAssignment().getEid();
            String ename = employeeService.getEmployeeByEid(eid).getEname();
            Date date = attendance.getWorkAssignment().getPlanDetail().getDate();
            int shiftId = attendance.getWorkAssignment().getPlanDetail().getShift().getSid();
            double alpha = attendance.getAlpha();

            EmployeeSalaryReport report = reportMap.computeIfAbsent(eid, id -> {
                EmployeeSalaryReport newReport = new EmployeeSalaryReport();
                newReport.setEid(eid);
                newReport.setEname(ename);
                newReport.setDailyShiftAlphaMap(new HashMap<>());
                return newReport;
            });

            // Thêm alpha cho từng ca làm việc trong một ngày
            report.addShiftAlpha(date, shiftId, alpha);
        }

        // Tính toán tổng lương cho từng nhân viên dựa trên alpha của từng ca
        for (EmployeeSalaryReport report : reportMap.values()) {
            double baseSalary = employeeService.getWorkerByEid(report.getEid()).getSalary().getSalary();
            report.calculateTotalSalary(baseSalary);
            report.calculateTotalAlpha();
        }

        // Chuyển đổi map thành list và thêm vào model
        List<EmployeeSalaryReport> salaryReports = new ArrayList<>(reportMap.values());
        model.addAttribute("salaryReports", salaryReports);

        // Thiết lập danh sách ngày trong tháng cho bảng
        YearMonth yearMonth = YearMonth.of(year, month);
        List<LocalDate> dates = new ArrayList<>();
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            dates.add(LocalDate.of(year, month, day));
        }
        model.addAttribute("dates", dates);

        salaryReports.iterator().forEachRemaining(System.out::println);

        return "salaryreport";
    }
}