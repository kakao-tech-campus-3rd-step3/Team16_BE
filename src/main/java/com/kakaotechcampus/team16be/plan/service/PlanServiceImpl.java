package com.kakaotechcampus.team16be.plan.service;

import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.notification.service.NotificationService;
import com.kakaotechcampus.team16be.plan.PlanRepository;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.plan.dto.PlanRequestDto;
import com.kakaotechcampus.team16be.plan.dto.PlanResponseDto;
import com.kakaotechcampus.team16be.plan.exception.PlanErrorCode;
import com.kakaotechcampus.team16be.plan.exception.PlanException;
import com.kakaotechcampus.team16be.planParticipant.dto.PlanParticipantResponseDto;
import com.kakaotechcampus.team16be.planParticipant.service.PlanParticipantService;
import com.kakaotechcampus.team16be.user.domain.User;
import java.time.LocalDateTime;
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
    private final NotificationService notificationService;
    private final GroupMemberService groupMemberService;


    @Override
    @Transactional
    public void updatePlan(User user, Long groupId, Long planId, PlanRequestDto planRequestDto) {
        Group group = groupService.findGroupById(groupId);
        group.checkLeader(user);

        Plan plan = planRepository.findByGroupIdAndId(groupId, planId)
                .orElseThrow(() -> new PlanException(PlanErrorCode.PLAN_NOT_FOUND));

        plan.changePlan(planRequestDto);
        List<GroupMember> members = groupMemberService.findByGroup(plan.getGroup());

        notificationService.createPlanUpdateNotifications(plan, members);
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
