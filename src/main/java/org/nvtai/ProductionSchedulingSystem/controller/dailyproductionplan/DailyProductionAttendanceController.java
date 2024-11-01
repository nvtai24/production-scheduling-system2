package org.nvtai.ProductionSchedulingSystem.controller.dailyproductionplan;

import org.nvtai.ProductionSchedulingSystem.dto.AttendanceDTO;
import org.nvtai.ProductionSchedulingSystem.dto.AttendanceForm;
import org.nvtai.ProductionSchedulingSystem.entity.Attendance;
import org.nvtai.ProductionSchedulingSystem.entity.Employee;
import org.nvtai.ProductionSchedulingSystem.entity.WorkAssignment;
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

import java.util.*;
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
            @RequestParam("plid") Integer plid,
            @RequestParam("date") String date,
            @RequestParam("sid") Integer sid,
            Model model) {

        List<WorkAssignment> assignments = workAssignmentService.getAssignmentsByPlanIdAndDate(plid, date, sid);

        assignments = assignments.stream()
                .filter(assignment -> assignment.getQuantity() > 0)
                .sorted(Comparator.comparing(WorkAssignment::getEid).thenComparing(assignment -> assignment
                        .getPlanDetail()
                        .getPlanHeader()
                        .getProduct()
                        .getPid()))
                .collect(Collectors.toList());

        Map<Integer, String> employeeMap = new HashMap<>();
        List<AttendanceDTO> attendanceList = new ArrayList<>();

        if (!assignments.isEmpty()) {
            List<Employee> workers = employeeService.getWorkersByDid(assignments
                    .get(0)
                    .getPlanDetail()
                    .getPlanHeader()
                    .getPlan()
                    .getDepartment()
                    .getDid());
            employeeMap = workers.stream().collect(Collectors.toMap(Employee::getEid, Employee::getEname));

            for (WorkAssignment assignment : assignments) {
                AttendanceDTO dto = new AttendanceDTO();
                dto.setWaid(assignment.getWaid());

                // Kiểm tra nếu có dữ liệu attendance trước đó
                List<Attendance> attendances = attendanceService.findByWorkAssignmentWaid(assignment.getWaid());
                if (!attendances.isEmpty()) {
                    Attendance attendance = attendances.get(0);
                    dto.setAtid(attendance.getAtid());
                    dto.setActualQuantity(attendance.getActualquantity());
                    dto.setAlpha(attendance.getAlpha());
                    dto.setNote(attendance.getNote());
                } else {
                    dto.setAtid(null);
                    dto.setActualQuantity(null);
                    dto.setAlpha(null);
                    dto.setNote("");
                }

                attendanceList.add(dto);
            }
        }

        AttendanceForm attendanceForm = new AttendanceForm();
        attendanceForm.setAttendanceList(attendanceList);

        model.addAttribute("date", date);
        model.addAttribute("plid", plid);

        model.addAttribute("employeeMap", employeeMap);
        model.addAttribute("assignments", assignments);
        model.addAttribute("attendanceForm", attendanceForm);

        return "dailyproductionplan/attendance";
    }


    @PostMapping("/dailyproduction/attendance")
    public String saveAttendance(
            @ModelAttribute AttendanceForm attendanceForm,
            @RequestParam("plid") Integer plid,
            @RequestParam("date") String date) {
        List<AttendanceDTO> attendanceList = attendanceForm.getAttendanceList();

        attendanceService.save(attendanceList);

        return "redirect:/showdailydetail?plid=" + plid + "&date=" + date;
    }
}