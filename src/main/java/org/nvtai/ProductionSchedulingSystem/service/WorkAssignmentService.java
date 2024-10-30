package org.nvtai.ProductionSchedulingSystem.service;

import org.nvtai.ProductionSchedulingSystem.entity.PlanDetail;
import org.nvtai.ProductionSchedulingSystem.entity.Product;
import org.nvtai.ProductionSchedulingSystem.entity.WorkAssignment;
import org.nvtai.ProductionSchedulingSystem.repository.WorkAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class WorkAssignmentService {

    @Autowired
    private WorkAssignmentRepository workAssignmentRepository;

    @Autowired
    private PlanDetailService planDetailService;

    public void saveWorkAssignments(Integer planId, String date, Map<String, String> allParams) {

        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            // Xử lý key employee_<shiftId>_<index>
            if (entry.getKey().startsWith("employee_")) {
                String[] keyParts = entry.getKey().split("_");
                String shiftId = keyParts[1]; // Lấy shiftId
                String index = keyParts[2]; // Lấy chỉ số index

                String employeeId = entry.getValue(); // Lấy employeeId (eid) từ giá trị

                // Sau khi lấy employeeId, lặp qua allParams để tìm các quantity tương ứng
                for (Map.Entry<String, String> quantityEntry : allParams.entrySet()) {
                    // Xử lý key quantity_<shiftId>_<index>_<productId>
                    if (quantityEntry.getKey().startsWith("quantity_" + shiftId + "_" + index)) {
                        String[] quantityKeyParts = quantityEntry.getKey().split("_");
                        String productId = quantityKeyParts[3]; // Lấy productId (pid)
                        String quantityStr = quantityEntry.getValue(); // Lấy số lượng

                        // Kiểm tra xem quantity có hợp lệ hay không
                        int quantity =
                                (quantityStr != null && !quantityStr.isEmpty()) ? Integer.parseInt(quantityStr) : 0;

                        // Nếu employeeId và quantity hợp lệ, thực hiện xử lý tiếp
                        if (employeeId != null && !employeeId.isEmpty() && quantity > 0) {
                            // Tìm PlanDetail dựa trên planId, productId, shiftId, và date
                            PlanDetail planDetail = planDetailService.getPlanDetail(
                                    planId, Integer.parseInt(productId), Integer.parseInt(shiftId), date);

                            if (planDetail != null) {
                                // Tạo WorkAssignment và lưu vào database
                                WorkAssignment workAssignment = new WorkAssignment();
                                workAssignment.setPlanDetail(planDetail);
                                workAssignment.setEid(Integer.parseInt(employeeId));
                                workAssignment.setQuantity(quantity);

                                // Lưu WorkAssignment vào database
                                workAssignmentRepository.save(workAssignment);
                            }
                        }
                    }
                }
            }
        }
    }

    public List<WorkAssignment> getAssignmentsByPlanIdAndDate(Integer planId, String date) {
        // Logic để lấy dữ liệu từ cơ sở dữ liệu dựa vào planId và date

        List<PlanDetail> planDetails = planDetailService.getPlanDetails(planId, date);

        List<WorkAssignment> workAssignments = new ArrayList<>();

        for (PlanDetail planDetail : planDetails) {
            List<WorkAssignment> assignments = workAssignmentRepository.findByPlanDetailPdid(planDetail.getPdid());
            workAssignments.addAll(assignments);
        }
        return workAssignments;
    }
}