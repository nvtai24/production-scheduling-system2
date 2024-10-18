package org.nvtai.ProductionSchedulingSystem.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.nvtai.ProductionSchedulingSystem.dto.ProductionPlanDTO;
import org.nvtai.ProductionSchedulingSystem.entity.Plan;
import org.nvtai.ProductionSchedulingSystem.entity.PlanHeader;

@Mapper
public interface PlanMapper {

//    PlanMapper INSTANCE = Mappers.getMapper(PlanMapper.class);
//
//
//    @Mapping(source = "planname", target = "plname")
//    @Mapping(source = "from", target = "startdate")
//    @Mapping(source = "to", target = "enddate")
//    @Mapping(target = "isdeleted", ignore = true)
//    @Mapping(target = "note", source = "note")
//    Plan dtoToPlan(ProductionPlanDTO dto);
//
//
//    @Mapping(source = "quantity", target = "quantity")
//    @Mapping(source = "effort", target = "estimatedeffort")
//    @Mapping(target = "product", ignore = true)
//    PlanHeader dtoToPlanHeader(ProductionPlanDTO dto, Plan plan, Integer productId, Integer quantity, Float effort);
}