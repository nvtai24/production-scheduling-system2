package org.nvtai.ProductionSchedulingSystem.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DailyProductionSummaryDTO {
    Date date;
    Integer productId;
    String productName;
    Integer totalQuantity;
}