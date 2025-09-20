package com.kakaotechcampus.team16be.attend.service;

import com.kakaotechcampus.team16be.attend.controller.RequestAttendDto;
import com.kakaotechcampus.team16be.attend.domain.Attend;
import com.kakaotechcampus.team16be.attend.repository.AttendRepository;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.plan.service.PlanService;
import com.kakaotechcampus.team16be.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
