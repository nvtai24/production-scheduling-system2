package org.nvtai.ProductionSchedulingSystem.service;

import org.nvtai.ProductionSchedulingSystem.entity.Plan;
import org.nvtai.ProductionSchedulingSystem.entity.PlanDetail;
import org.nvtai.ProductionSchedulingSystem.entity.PlanHeader;
import org.nvtai.ProductionSchedulingSystem.repository.PlanDetailRepository;
import org.nvtai.ProductionSchedulingSystem.repository.PlanHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlanDetailService {

    @Autowired
    private PlanDetailRepository planDetailRepository;

    public List<PlanDetail> getPlanDetailsByPlanHeaderId(Integer phid) {
        return planDetailRepository.findByPlanHeaderPhid(phid);
    }

    public List<PlanDetail> getPlanDetailsByPlanHeaderIdAndDate(Integer phid, java.sql.Date date) {
        return planDetailRepository.findByPlanHeaderPhidAndDate(phid, date);
    }

    public void savePlanDetail(PlanDetail planDetail) {
        PlanDetail existedPlanDetail = planDetailRepository.findByPlanHeaderPhidAndShiftSidAndDate(
                planDetail.getPlanHeader().getPhid(), planDetail.getShift().getSid(), planDetail.getDate());
        if (existedPlanDetail != null) {
            existedPlanDetail.setQuantity(planDetail.getQuantity());
            planDetailRepository.save(existedPlanDetail);
        } else {
            planDetailRepository.save(planDetail);
        }
    }

    @Autowired
    private PlanHeaderRepository planHeaderRepository;

    public Map<String, Integer> getPreviousPlanDetails(Integer planId) {
        // Lấy danh sách PlanHeader dựa trên planId
        List<PlanHeader> planHeaders = planHeaderRepository.findByPlanPlid(planId);

        Map<String, Integer> previousSchedules = new HashMap<>();

        // Duyệt qua từng PlanHeader
        for (PlanHeader planHeader : planHeaders) {
            // Lấy danh sách PlanDetail từ PlanHeader
            List<PlanDetail> details = planDetailRepository.findByPlanHeaderPhid(planHeader.getPhid());

            // Duyệt qua từng PlanDetail và lưu vào Map
            for (PlanDetail detail : details) {
                String formattedDate = detail.getDate()
                        .toLocalDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Đảm bảo định dạng khớp
                String key =
                        "day:" + formattedDate + ",shift:" + detail.getShift().getSid() + ",product:"
                                + detail.getPlanHeader().getProduct().getPid();
                previousSchedules.put(key, detail.getQuantity());
            }
        }

        return previousSchedules;
    }

    public List<PlanDetail> getPlanDetails(Integer planId, String date) {
        ArrayList<PlanHeader> headers = new ArrayList<>(planHeaderRepository.findByPlanPlid(planId));

        List<PlanDetail> details = new ArrayList<>();

        for (PlanHeader header : headers) {
            details.addAll(
                planDetailRepository.findByPlanHeaderPhidAndDate(header.getPhid(), java.sql.Date.valueOf(date)));
        }

        return details;
    }

    public PlanDetail getPlanDetail(Integer planId, Integer shiftId, Integer productId, String date) {
        PlanHeader planHeader = planHeaderRepository.findByPlanPlidAndProductPid(planId, productId);
        return planDetailRepository.findByPlanHeaderPhidAndShiftSidAndDate(planHeader.getPhid(), shiftId, java.sql.Date.valueOf(date));
    }
}