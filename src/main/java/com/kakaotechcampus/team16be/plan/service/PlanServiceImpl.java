package com.kakaotechcampus.team16be.plan.service;

import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import com.kakaotechcampus.team16be.common.eventListener.groupEvent.IncreaseGroupScoreByPosting;
import com.kakaotechcampus.team16be.common.eventListener.groupEvent.IncreaseScoreByPlanning;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.notification.repository.NotificationRepository;
import com.kakaotechcampus.team16be.notification.service.NotificationService;
import com.kakaotechcampus.team16be.plan.domain.Location;
import com.kakaotechcampus.team16be.plan.PlanRepository;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.plan.dto.PlanRequestDto;
import com.kakaotechcampus.team16be.plan.dto.PlanResponseDto;
import com.kakaotechcampus.team16be.plan.exception.PlanErrorCode;
import com.kakaotechcampus.team16be.plan.exception.PlanException;
import com.kakaotechcampus.team16be.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanServiceImpl implements PlanService {


    private final PlanRepository planRepository;
    private final GroupService groupService;
    private final NotificationService notificationService;
    private final GroupMemberService groupMemberService;
    private final S3UploadPresignedUrlService s3UploadPresignedUrlService;
    private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public Long createPlan(User user, Long groupId, PlanRequestDto planRequestDto) {

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
                    .coverImg(planRequestDto.coverImageUrl())
                    .location(location)
                    .build();
    Plan savedPlan = planRepository.save(plan);

    eventPublisher.publishEvent(new IncreaseScoreByPlanning(group));
    return savedPlan.getId();
  }

  @Override
  public PlanResponseDto getPlan(Long groupId, Long planId) {
    Plan plan = planRepository.findByGroupIdAndId(groupId, planId)
                              .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND));

    String fullUrl = s3UploadPresignedUrlService.getPublicUrl(plan.getCoverImg());

    return PlanResponseDto.from(plan, fullUrl);
  }

  @Override
  public List<PlanResponseDto> getAllPlans(Long groupId) {
    return planRepository.findByGroupId(groupId)
            .stream()
            .map(plan -> {
              String fullUrl = s3UploadPresignedUrlService.getPublicUrl(plan.getCoverImg());
              return PlanResponseDto.from(plan, fullUrl);
            })
            .toList();
  }

  @Override
  @Transactional
  public void updatePlan(User user, Long groupId, Long planId, PlanRequestDto planRequestDto) {
    Group group = groupService.findGroupById(groupId);
    group.checkLeader(user);

    Plan plan = planRepository.findByGroupIdAndId(groupId, planId)
                              .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND));

    plan.changePlan(planRequestDto);
      List<GroupMember> members = groupMemberService.findByGroup(plan.getGroup());

    notificationService.createPlanUpdateNotifications(plan,members);
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

    @Transactional
    @Override
    public List<Plan> findAllByEndTimeBetween(LocalDateTime fiveMinutesAgo, LocalDateTime now) {
        return planRepository.findAllByEndTimeBetween(fiveMinutesAgo, now);
    }
}
