package com.kakaotechcampus.team16be.plan.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.plan.domain.Location;
import com.kakaotechcampus.team16be.plan.PlanRepository;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.plan.dto.PlanRequestDto;
import com.kakaotechcampus.team16be.plan.dto.PlanResponseDto;
import com.kakaotechcampus.team16be.plan.exception.PlanErrorCode;
import com.kakaotechcampus.team16be.plan.exception.PlanException;
import com.kakaotechcampus.team16be.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanServiceImpl implements PlanService {

  private final PlanRepository planRepository;
  private final GroupService groupService;

  @Override
  @Transactional
  public PlanResponseDto createPlan(User user, Long groupId, PlanRequestDto planRequestDto) {

    Group group = groupService.findGroupById(groupId);
    group.checkLeader(user);

    Location location = Location.builder()
                                .name(planRequestDto.location().name())
                                .latitude(planRequestDto.location().latitude())
                                .longitude(planRequestDto.location().longitude())
                                .build();

    Plan plan = Plan.builder()
                    .group(group)
                    .title(planRequestDto.title())
                    .description(planRequestDto.description())
                    .capacity(planRequestDto.capacity())
                    .startTime(planRequestDto.startTime())
                    .endTime(planRequestDto.endTime())
                    .location(location)
                    .build();

    Plan savedPlan = planRepository.save(plan);
    return PlanResponseDto.from(savedPlan);
  }

  @Override
  public PlanResponseDto getPlan(Long groupId, Long planId) {
    Plan plan = planRepository.findByGroupIdAndId(groupId, planId)
                              .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND));
    return PlanResponseDto.from(plan);
  }

  @Override
  public List<PlanResponseDto> getAllPlans(Long groupId) {
    return planRepository.findByGroupId(groupId)
                         .stream()
                         .map(PlanResponseDto::from)
                         .toList();
  }

  @Override
  @Transactional
  public PlanResponseDto updatePlan(User user, Long groupId, Long planId, PlanRequestDto planRequestDto) {
    Group group = groupService.findGroupById(groupId);
    group.checkLeader(user);

    Plan plan = planRepository.findByGroupIdAndId(groupId, planId)
                              .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND));

    plan.changePlan(planRequestDto);
    return PlanResponseDto.from(plan);
  }

  @Override
  @Transactional
  public void deletePlan(User user, Long groupId, Long planId) {
    Group group = groupService.findGroupById(groupId);
    group.checkLeader(user);

    Plan plan = planRepository.findByGroupIdAndId(groupId, planId)
                              .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND));

    planRepository.delete(plan);
  }

  @Override
  public Plan findByGroupIdAndPlanId(Long groupId, Long planId) {
    return planRepository.findByGroupIdAndId(groupId, planId)
                         .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND));
  }

  public Plan findById(Long userId) {

    return planRepository.findById(userId)
                         .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND));

  }
}
