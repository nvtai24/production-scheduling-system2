package org.nvtai.ProductionSchedulingSystem.controller.productionplan;

import org.nvtai.ProductionSchedulingSystem.dto.ProductionPlanDetailDTO;
import org.nvtai.ProductionSchedulingSystem.entity.*;
import org.nvtai.ProductionSchedulingSystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
public class ProductionPlanScheduleController {

    @Autowired
    private PlanService planService;

    @Autowired
    private ProductionPlanDetailService productionPlanDetailService;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PlanHeaderService planHeaderService;

    @Autowired
    private PlanDetailService planDetailService;

    @GetMapping("/schedule")
    public String showListPlan(@RequestParam("plid") Integer plid, Model model) {
        ProductionPlanDetailDTO planDetails = productionPlanDetailService.getProductionPlanDetail(plid);

        List<Shift> shifts = shiftService.getAllShifts();
        List<Product> products = productService.getAllProducts();

        List<String> formattedDays = listFormatDays(planDetails.getPlan().getStartdate(), planDetails.getPlan().getEnddate());

        // Lấy danh sách PlanDetail đã lập trước đó
        Map<String, Integer> previousSchedules = planDetailService.getPreviousPlanDetails(plid);

        previousSchedules.forEach((key, value) -> {
            System.out.println(key + " " + value);
        });

        model.addAttribute("formattedDays", formattedDays);
        model.addAttribute("shifts", shifts);
        model.addAttribute("products", products);
        model.addAttribute("planDetails", planDetails);
        model.addAttribute("previousSchedules", previousSchedules);

        return "plan/schedule";
    }

    private List<String> listFormatDays(Date startDate, Date endDate) {
        LocalDate start = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();
        long numOfDays = ChronoUnit.DAYS.between(start, end) + 1;
        List<String> formattedDays = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Sử dụng định dạng 'yyyy-MM-dd'
        for (int i = 0; i < numOfDays; i++) {
            formattedDays.add(start.plusDays(i).format(formatter)); // Lưu ngày theo định dạng chuẩn 'yyyy-MM-dd'
        }
        return formattedDays;
    }


    @PostMapping("/schedule")
    public String saveSchedule(@RequestParam("planId") Integer planId,
                               @RequestParam Map<String, String> quantities) {

        quantities.forEach((key, value) -> {
            if (!key.equals("planId") && !value.equals("")) {  // Bỏ qua key "planId" và các giá trị trống
                String[] keys = key.replace("quantities[", "").replace("]", "").split("\\[");
                int dayIndex = Integer.parseInt(keys[0]);
                Integer shiftId = Integer.parseInt(keys[1]);
                Integer productId = Integer.parseInt(keys[2]);

                int quantity = Integer.parseInt(value);

                PlanHeader planHeader = planHeaderService.getPlanHeaderIdByPlanIdAndProductId(planId, productId);

                LocalDate date = planService.get(planId).getStartdate().toLocalDate().plusDays(dayIndex);

                PlanDetail planDetail = new PlanDetail();
                planDetail.setPlanHeader(planHeader);
                planDetail.setDate(Date.valueOf(date));
                planDetail.setShift(shiftService.getShiftById(shiftId));
                planDetail.setQuantity(quantity);

                planDetailService.savePlanDetail(planDetail);
            }
        });

        return "redirect:/plan";
    }
}