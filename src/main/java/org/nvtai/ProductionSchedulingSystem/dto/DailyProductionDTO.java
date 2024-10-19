package org.nvtai.ProductionSchedulingSystem.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DailyProductionDTO {
    Date date;
    int productId;
    String productName;
    String shift;
    int quantity;
    String note;
}