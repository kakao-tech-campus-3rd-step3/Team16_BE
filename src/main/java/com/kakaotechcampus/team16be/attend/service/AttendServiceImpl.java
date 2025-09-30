package com.kakaotechcampus.team16be.attend.service;

import com.kakaotechcampus.team16be.attend.dto.RequestAttendDto;
import com.kakaotechcampus.team16be.attend.domain.Attend;
import com.kakaotechcampus.team16be.attend.repository.AttendRepository;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.plan.service.PlanService;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AttendServiceImpl implements AttendService{

    private final AttendRepository attendRepository;
    private final PlanService planService;
    private final GroupMemberService groupMemberService;
    private final GroupService groupService;

    @Transactional
    @Override
    public Attend attendGroup(User user, Long groupId, RequestAttendDto requestAttendDto) {
        Group targetGroup = groupService.findGroupById(groupId);
        GroupMember groupMember = groupMemberService.findByGroupAndUser(targetGroup, user);
        Plan plan = planService.findByGroupIdAndPlanId(groupId, requestAttendDto.planId());

        Attend attend = Attend.attendPlan(groupMember, plan);

        return attendRepository.save(attend);
    }

    /***
     *  조회 권한은 그룹장만 가능?
     */
    @Transactional(readOnly = true)
    @Override
    public List<Attend> getAllAttends(User user, Long groupId, Long planId) {
        Group targetGroup = groupService.findGroupById(groupId);

        targetGroup.checkLeader(user);
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

        return attendRepository.findByPlanAndGroupMember(plan, groupMember);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Attend> getAbsentMembers(User user, Long groupId, Long planId) {
        Group targetGroup = groupService.findGroupById(groupId);
        targetGroup.checkLeader(user);

        Plan plan = planService.findById(planId);

        List<GroupMember> allGroupMembers = groupMemberService.findByGroup(targetGroup);

        List<Attend> presentAttends = attendRepository.findAllByPlan(plan);

        Set<GroupMember> presentMembers = presentAttends.stream()
                .map(Attend::getGroupMember)
                .collect(Collectors.toSet());


        return allGroupMembers.stream()
                .filter(member -> !presentMembers.contains(member))
                .map(absentMember -> Attend.builder()
                        .groupMember(absentMember)
                        .plan(plan)
                        .build())
                .toList();
    }
}
