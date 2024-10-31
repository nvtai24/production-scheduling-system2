package org.nvtai.ProductionSchedulingSystem.service;

import org.nvtai.ProductionSchedulingSystem.dto.AttendanceDTO;
import org.nvtai.ProductionSchedulingSystem.entity.Attendance;
import org.nvtai.ProductionSchedulingSystem.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    public Attendance findByWorkAssignmentWaid(Integer waid) {
        return attendanceRepository.findByWorkAssignmentWaid(waid);
    }

    void saveAttendance(Attendance attendance) {
        attendanceRepository.save(attendance);
    }



    @Autowired
    private WorkAssignmentService workAssignmentService;

    public void save(List<AttendanceDTO> attendanceList) {
        for (AttendanceDTO dto : attendanceList) {
            Attendance attendance;
            if (dto.getAtid() != null) {
                // Tìm bản ghi cũ dựa trên atid
                attendance = attendanceRepository.findById(dto.getAtid()).orElse(null);
                if (attendance == null) {
                    // Nếu không tìm thấy trong database, tạo mới
                    attendance = new Attendance();
                    attendance.setWorkAssignment(workAssignmentService.getWorkAssignmentById(dto.getWaid()));
                }
            } else {
                // Tạo mới nếu atid là null
                attendance = new Attendance();
                attendance.setWorkAssignment(workAssignmentService.getWorkAssignmentById(dto.getWaid()));
            }

            // Cập nhật thông tin chấm công
            attendance.setActualquantity(dto.getActualQuantity());
            attendance.setAlpha(dto.getAlpha());
            attendance.setNote(dto.getNote());

            attendanceRepository.save(attendance);
        }
    }

}