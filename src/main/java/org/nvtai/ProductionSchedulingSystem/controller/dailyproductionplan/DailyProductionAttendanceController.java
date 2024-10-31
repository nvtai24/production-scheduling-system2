package org.nvtai.ProductionSchedulingSystem.controller.dailyproductionplan;

import org.nvtai.ProductionSchedulingSystem.dto.AttendanceDTO;
import org.nvtai.ProductionSchedulingSystem.dto.AttendanceForm;
import org.nvtai.ProductionSchedulingSystem.entity.Attendance;
import org.nvtai.ProductionSchedulingSystem.entity.Employee;
import org.nvtai.ProductionSchedulingSystem.entity.WorkAssignment;
import org.nvtai.ProductionSchedulingSystem.repository.AttendanceRepository;
import org.nvtai.ProductionSchedulingSystem.service.AttendanceService;
import org.nvtai.ProductionSchedulingSystem.service.EmployeeService;
import org.nvtai.ProductionSchedulingSystem.service.WorkAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DailyProductionAttendanceController {

    @Autowired
    private WorkAssignmentService workAssignmentService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/dailyproduction/attendance")
    public String showDailyProductionAttendance(
            @RequestParam("plid") Integer planId,
            @RequestParam("date") String date,
            @RequestParam("sid") Integer sid,
            Model model) {

        List<WorkAssignment> assignments = workAssignmentService.getAssignmentsByPlanIdAndDate(planId, date, sid);
        Map<Integer, String> employeeMap = new HashMap<>();
        List<AttendanceDTO> attendanceList = new ArrayList<>();

        if (!assignments.isEmpty()) {
            List<Employee> workers = employeeService.getWorkersByDid(assignments
                    .getFirst()
                    .getPlanDetail()
                    .getPlanHeader()
                    .getPlan()
                    .getDepartment()
                    .getDid());
            employeeMap = workers.stream().collect(Collectors.toMap(Employee::getEid, Employee::getEname));

            // Lấy dữ liệu attendance cũ cho từng WorkAssignment
            for (WorkAssignment assignment : assignments) {
                Attendance attendance = attendanceService.findByWorkAssignmentWaid(assignment.getWaid());
                AttendanceDTO dto = new AttendanceDTO();
                dto.setAtid(attendance != null ? attendance.getAtid() : null);
                dto.setWaid(assignment.getWaid());
                dto.setActualQuantity(attendance != null ? attendance.getActualquantity() : null);
                dto.setAlpha(attendance != null ? attendance.getAlpha() : null);
                dto.setNote(attendance != null ? attendance.getNote() : "");

                attendanceList.add(dto);
            }
        }
        AttendanceForm attendanceForm = new AttendanceForm();
        attendanceForm.setAttendanceList(attendanceList);

        model.addAttribute("employeeMap", employeeMap);
        model.addAttribute("assignments", assignments);
        model.addAttribute("attendanceForm", attendanceForm);

        return "dailyproductionplan/attendance";
    }



    @PostMapping("/dailyproduction/attendance")
    public String saveAttendance(@ModelAttribute AttendanceForm attendanceForm) {
        List<AttendanceDTO> attendanceList = attendanceForm.getAttendanceList();

        attendanceService.save(attendanceList);

        return "redirect:/dailyproduction/attendance";
    }
}