package org.nvtai.ProductionSchedulingSystem.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceDTO {
    Integer atid;
    int waid;
    Integer actualQuantity;
    Float alpha;
    String note;
}