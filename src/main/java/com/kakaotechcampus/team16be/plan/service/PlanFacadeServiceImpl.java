package com.kakaotechcampus.team16be.plan.service;

import com.kakaotechcampus.team16be.attend.service.AttendService;
import com.kakaotechcampus.team16be.common.eventListener.groupEvent.IncreaseScoreByPlanning;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.plan.PlanRepository;
import com.kakaotechcampus.team16be.plan.domain.Location;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.plan.dto.PlanRequestDto;
import com.kakaotechcampus.team16be.user.domain.User;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PlanFacadeServiceImpl {

    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final PlanRepository planRepository;
    private final AttendService attendService;
    final ApplicationEventPublisher eventPublisher;

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

        List<GroupMember> targetGroupMembers = groupMemberService.findByGroup(group);

        Plan savedPlan = planRepository.save(plan);

        for (GroupMember targetGroupMember : targetGroupMembers) {
            attendService.attendPending(targetGroupMember, plan);
        }

        eventPublisher.publishEvent(new IncreaseScoreByPlanning(group));
        return savedPlan.getId();
    }
}
