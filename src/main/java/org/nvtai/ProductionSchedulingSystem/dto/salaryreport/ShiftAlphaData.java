package org.nvtai.ProductionSchedulingSystem.dto.salaryreport;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShiftAlphaData {

    double totalShiftAlpha;
    int count;

    public ShiftAlphaData(double alpha) {
        this.totalShiftAlpha = alpha;
        this.count = 1;
    }

    public void addShiftAlpha(double alpha) {
        this.totalShiftAlpha += alpha;
        this.count++;
    }

    public double getAverageAlpha() {
        return this.count > 0 ? this.totalShiftAlpha / this.count : 0;
    }
}