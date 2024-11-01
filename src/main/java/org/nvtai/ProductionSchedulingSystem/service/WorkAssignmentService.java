package org.nvtai.ProductionSchedulingSystem.service;

import org.nvtai.ProductionSchedulingSystem.entity.PlanDetail;
import org.nvtai.ProductionSchedulingSystem.entity.Product;
import org.nvtai.ProductionSchedulingSystem.entity.WorkAssignment;
import org.nvtai.ProductionSchedulingSystem.repository.WorkAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WorkAssignmentService {

    @Autowired
    private WorkAssignmentRepository workAssignmentRepository;

    @Autowired
    private PlanDetailService planDetailService;

    @Autowired
    private PlanHeaderService planHeaderService;

    public WorkAssignment getWorkAssignmentById(Integer waid) {
        return workAssignmentRepository.findById(waid).orElse(null);
    }

    public List<WorkAssignment> getWorkAssignmentsByPlanDetailId(Integer pdid) {
        return workAssignmentRepository.findByPlanDetailPdid(pdid);
    }

    public void saveWorkAssignments(Integer planId, String date, Map<String, String> allParams) {

        Pattern pattern = Pattern.compile("assignments\\[(\\d+)\\]\\[(\\d+)]\\.quantities\\[(\\d+)]");

        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            System.out.println("Processing: " + entry.getKey() + " : " + entry.getValue());

            Matcher matcher = pattern.matcher(entry.getKey());

            if (matcher.find()) {
                int shiftId = Integer.parseInt(matcher.group(1));
                int employeeId = Integer.parseInt(matcher.group(2));
                int productId = Integer.parseInt(matcher.group(3));

                Integer quantity = entry.getValue() != null && !entry.getValue().isEmpty()
                        ? Integer.parseInt(entry.getValue())
                        : null;

                System.out.println("Extracted shiftId: " + shiftId + ", employeeId: " + employeeId + ", productId: "
                        + productId + ", quantity: " + quantity);

                // Bỏ qua nếu quantity == null
                if (quantity == null) {
                    continue;
                }

                if (quantity >= 0) {
                    try {
                        Integer phid = planHeaderService
                                .getPlanHeaderIdByPlanIdAndProductId(planId, productId)
                                .getPhid();

                        if (phid == null) {
                            System.out.println("No phid found for planId " + planId + " and productId " + productId);
                            continue;
                        }

                        PlanDetail planDetail = planDetailService.getPlanDetail(phid, shiftId, date);

                        if (planDetail == null) {
                            System.out.println(
                                    "No PlanDetail found for phid " + phid + ", shiftId " + shiftId + ", date " + date);
                            continue;
                        }

                        // Kiểm tra xem WorkAssignment đã tồn tại cho eid và pdid chưa
                        WorkAssignment existingAssignment =
                                workAssignmentRepository.findByPlanDetailPdidAndEid(planDetail.getPdid(), employeeId);

                        if (existingAssignment != null) {
                            // Cập nhật bản ghi nếu đã tồn tại
                            System.out.println(
                                    "Updating existing WorkAssignment with id: " + existingAssignment.getWaid());
                        } else {
                            // Tạo bản ghi mới nếu chưa tồn tại
                            existingAssignment = new WorkAssignment();
                            System.out.println("Creating new WorkAssignment");
                        }

                        // Thiết lập giá trị cho WorkAssignment
                        existingAssignment.setPlanDetail(planDetail);
                        existingAssignment.setEid(employeeId);
                        existingAssignment.setQuantity(quantity);

                        workAssignmentRepository.save(existingAssignment);
                        System.out.println("Saved or updated WorkAssignment: " + existingAssignment);
                    } catch (Exception e) {
                        System.err.println("Error processing assignment: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("Key does not match the expected pattern: " + entry.getKey());
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

    public List<WorkAssignment> getAssignmentsByPlanIdAndDate(Integer planId, String date, Integer shiftId) {
        // Lấy dữ liệu từ cơ sở dữ liệu dựa vào planId, date và shiftId
        List<PlanDetail> planDetails = planDetailService.getPlanDetails(planId, date, shiftId);

        // Kiểm tra nếu planDetails là null
        if (planDetails == null || planDetails.isEmpty()) {
            System.out.println("No PlanDetails found for the given planId, date, and shiftId.");
            return new ArrayList<>(); // Trả về danh sách trống nếu không có PlanDetails
        }

        List<WorkAssignment> workAssignments = new ArrayList<>();

        // Duyệt qua từng PlanDetail và kiểm tra null trước khi truy cập
        for (PlanDetail planDetail : planDetails) {
            if (planDetail != null) {
                List<WorkAssignment> assignments = workAssignmentRepository.findByPlanDetailPdid(planDetail.getPdid());
                if (assignments != null) {
                    workAssignments.addAll(assignments);
                }
            }
        }
        return workAssignments;
    }
}