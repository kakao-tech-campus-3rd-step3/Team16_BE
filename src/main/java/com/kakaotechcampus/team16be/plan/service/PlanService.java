package com.kakaotechcampus.team16be.plan.service;


import com.kakaotechcampus.team16be.plan.dto.PlanRequestDto;
import com.kakaotechcampus.team16be.plan.dto.PlanResponseDto;
import java.util.List;

public interface PlanService {

  PlanResponseDto createPlan(PlanRequestDto planRequestDto);
  PlanResponseDto getPlan(Long planId);
  List<PlanResponseDto> getAllPlans();
  PlanResponseDto updatePlan(Long planId, PlanRequestDto planRequestDto);
  void deletePlan(Long planId);
}
