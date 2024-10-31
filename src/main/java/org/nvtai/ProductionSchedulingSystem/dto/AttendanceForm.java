package org.nvtai.ProductionSchedulingSystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class AttendanceForm {
    private List<AttendanceDTO> attendanceList;
}