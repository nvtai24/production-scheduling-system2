package org.nvtai.ProductionSchedulingSystem.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDTO {
    int eid;
    String ename;
    int did;
    String phonenumber;
    String address;
    int sid;
    boolean ismanager = false;
    boolean isdeleted = false;
}