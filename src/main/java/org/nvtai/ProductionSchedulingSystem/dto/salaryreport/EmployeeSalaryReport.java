package org.nvtai.ProductionSchedulingSystem.dto.salaryreport;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeSalaryReport {
    int eid;
    String ename;
    Map<Date, Map<Integer, ShiftAlphaData>> dailyShiftAlphaMap = new HashMap<>(); // Date -> Shift -> Alpha
    double totalAlpha;
    double totalSalary;

    public void addShiftAlpha(Date date, int shiftId, double alpha) {
        dailyShiftAlphaMap
                .computeIfAbsent(date, d -> new HashMap<>())
                .computeIfAbsent(shiftId, s -> new ShiftAlphaData(alpha))
                .addShiftAlpha(alpha);
    }

    public double calculateTotalAlphaForDate(Date date) {
        Map<Integer, ShiftAlphaData> shiftMap = dailyShiftAlphaMap.get(date);
        if (shiftMap != null) {
            return shiftMap.values().stream()
                    .mapToDouble(ShiftAlphaData::getAverageAlpha)
                    .sum();
        }
        return 0;
    }


    public void calculateTotalSalary(double baseSalary) {
        totalAlpha = 0;
        totalSalary = dailyShiftAlphaMap.entrySet().stream()
                .mapToDouble(entry -> {
                    Date date = entry.getKey();
                    Map<Integer, ShiftAlphaData> shiftMap = entry.getValue();

                    double dailyAlpha = shiftMap.values().stream()
                            .mapToDouble(ShiftAlphaData::getAverageAlpha) // Trung bình alpha cho mỗi ca trong ngày
                            .sum();

                    totalAlpha += dailyAlpha;
                    return dailyAlpha * 8 * baseSalary; // Tính lương cho ngày dựa trên tổng alpha của các ca
                })
                .sum();
    }

    public void calculateTotalAlpha() {
        totalAlpha = dailyShiftAlphaMap.values().stream()
                .flatMap(shiftMap -> shiftMap.values().stream())
                .mapToDouble(ShiftAlphaData::getAverageAlpha)
                .sum();
    }
}