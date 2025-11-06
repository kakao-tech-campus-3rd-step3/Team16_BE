package com.kakaotechcampus.team16be.plan.service;


import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.plan.dto.PlanRequestDto;
import com.kakaotechcampus.team16be.plan.dto.PlanResponseDto;
import com.kakaotechcampus.team16be.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;

public interface PlanService {

    PlanResponseDto getPlan(Long groupId, Long planId);

    List<PlanResponseDto> getAllPlans(Long groupId);

    void updatePlan(User user, Long groupId, Long planId, PlanRequestDto planRequestDto);

    void deletePlan(User user, Long groupId, Long planId);

    Plan findByGroupIdAndPlanId(Long groupId, Long planId);

    Plan findById(Long planId);

    List<Plan> findAllByEndTimeBetween(LocalDateTime fiveMinutesAgo, LocalDateTime now);

}
