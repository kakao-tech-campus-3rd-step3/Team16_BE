package com.kakaotechcampus.team16be.plan.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.plan.PlanRepository;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.plan.dto.PlanRequestDto;
import com.kakaotechcampus.team16be.plan.dto.PlanResponseDto;
import com.kakaotechcampus.team16be.plan.exception.PlanErrorCode;
import com.kakaotechcampus.team16be.plan.exception.PlanException;
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
  public PlanResponseDto createPlan(PlanRequestDto planRequestDto) {
    Group group = groupService.findGroupById(planRequestDto.groupId());

    Plan plan = Plan.builder()
                    .group(group)
                    .title(planRequestDto.title())
                    .description(planRequestDto.description())
                    .capacity(planRequestDto.capacity())
                    .startTime(planRequestDto.startTime())
                    .endTime(planRequestDto.endTime())
                    .build();

    Plan saved = planRepository.save(plan);
    return toDto(saved);
  }

  @Override
  public PlanResponseDto getPlan(Long planId) {
    Plan plan = planRepository.findById(planId)
        .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND));

    return toDto(plan);
  }

  @Override
  public List<PlanResponseDto> getAllPlans() {
    return planRepository.findAll()
        .stream()
        .map(this::toDto)
        .toList();
  }

  @Override
  @Transactional
  public PlanResponseDto updatePlan(Long planId, PlanRequestDto planRequestDto) {
    Group group = groupService.findGroupById(planRequestDto.groupId());

    Plan plan = planRepository.findById(planId)
                              .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND));

    plan.changePlan(planRequestDto);
    return toDto(plan);
  }

  @Override
  @Transactional
  public void deletePlan(Long planId) {
    planRepository.deleteById(planId);
  }

  public PlanResponseDto toDto(Plan plan){
    return new PlanResponseDto(
        plan.getGroup().getId(),
        plan.getTitle(),
        plan.getDescription(),
        plan.getCapacity(),
        plan.getStartTime(),
        plan.getEndTime()
    );
  }
}
