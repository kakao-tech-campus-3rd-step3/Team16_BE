package com.kakaotechcampus.team16be.attend.service;

import com.kakaotechcampus.team16be.attend.domain.AttendStatus;
import com.kakaotechcampus.team16be.attend.dto.RequestAttendDto;
import com.kakaotechcampus.team16be.attend.domain.Attend;
import com.kakaotechcampus.team16be.attend.exception.AttendErrorCode;
import com.kakaotechcampus.team16be.attend.exception.AttendException;
import com.kakaotechcampus.team16be.attend.repository.AttendRepository;
import com.kakaotechcampus.team16be.common.eventListener.IncreaseScoreByAttendance;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.plan.service.PlanService;
import com.kakaotechcampus.team16be.user.domain.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendServiceImpl implements AttendService{

    private final AttendRepository attendRepository;
    private final PlanService planService;
    private final GroupMemberService groupMemberService;
    private final GroupService groupService;
    private final ApplicationEventPublisher eventPublisher;

    private static final ZoneId SEOUL_ZONE_ID = ZoneId.of("Asia/Seoul");

    @Transactional
    @Override
    public Attend attendGroup(User user, Long groupId, RequestAttendDto requestAttendDto) {
        Group targetGroup = groupService.findGroupById(groupId);
        GroupMember groupMember = groupMemberService.findByGroupAndUser(targetGroup, user);
        Plan plan = planService.findByGroupIdAndPlanId(groupId, requestAttendDto.planId());

        Optional<Attend> existingAttend = attendRepository.findByPlanAndGroupMember(plan, groupMember);

        if (existingAttend.isPresent()) {
            throw new AttendException(AttendErrorCode.ATTEND_ALREADY_EXIST);
        }
        LocalDateTime startOfToday = LocalDate.now(SEOUL_ZONE_ID).atStartOfDay();
        boolean hasAlreadyAttendedToday = attendRepository.existsByGroupMember_UserAndCreatedAtAfter(user, startOfToday);

        Attend attend = Attend.attendPlan(groupMember, plan);
        Attend savedAttend = attendRepository.save(attend);

        if (!hasAlreadyAttendedToday) {
            eventPublisher.publishEvent(new IncreaseScoreByAttendance(user));
        }
        return savedAttend;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Attend> getAllAttends(User user, Long groupId, Long planId) {
        Plan plan = planService.findByGroupIdAndPlanId(groupId, planId);

        return attendRepository.findAllByPlanOrderByCreatedAtAsc(plan);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Attend> getAttendsByGroup(User user, Long groupId) {
        Group targetGroup = groupService.findGroupById(groupId);
        GroupMember groupMember = groupMemberService.findByGroupAndUser(targetGroup, user);

        return attendRepository.findAllByGroupMemberOrderByCreatedAtAsc(groupMember);
    }

    @Transactional(readOnly = true)
    @Override
    public Attend getAttendByPlan(User user, Long groupId, Long planId) {
        Group targetGroup = groupService.findGroupById(groupId);
        GroupMember groupMember = groupMemberService.findByGroupAndUser(targetGroup, user);
        Plan plan = planService.findByGroupIdAndPlanId(groupId, planId);
        Attend attend = attendRepository.findByPlanAndGroupMember(plan, groupMember)
                .orElseThrow(() -> new AttendException(AttendErrorCode.ATTEND_NOT_FOUND));
        return attend;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Attend> getAbsentMembers(User user, Long groupId, Long planId) {
        Group targetGroup = groupService.findGroupById(groupId);
        targetGroup.checkLeader(user);

        Plan plan = planService.findById(planId);

        return attendRepository.findAllByPlanAndAttendStatus(plan, AttendStatus.ABSENT);
    }

    @Transactional
    @Override
    public List<Attend> findAllByPlan(Plan plan) {
        return attendRepository.findAllByPlan(plan);
    }

    @Transactional
    @Override
    public void saveAll(List<Attend> absentAttendees) {
        attendRepository.saveAll(absentAttendees);
    }

}
