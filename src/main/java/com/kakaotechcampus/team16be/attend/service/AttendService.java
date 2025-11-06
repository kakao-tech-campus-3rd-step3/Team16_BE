package com.kakaotechcampus.team16be.attend.service;

import com.kakaotechcampus.team16be.attend.domain.AttendStatus;
import com.kakaotechcampus.team16be.attend.dto.RequestAttendDto;
import com.kakaotechcampus.team16be.attend.domain.Attend;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;

public interface AttendService {
    Attend attendGroup(User user, Long groupId, RequestAttendDto requestAttendDto);

    List<Attend> getAllAttends(User user, Long groupId, Long planId);

    List<Attend> getAttendsByGroup(User user, Long groupId);

    Attend getAttendByPlan(User user, Long groupId, Long planId);

    List<Attend> getAbsentMembers(User user, Long groupId, Long planId);

    List<Attend> findAllByPlan(Plan plan);

    void saveAll(List<Attend> absentAttendees);

    void attendPending(GroupMember targetGroupMember, Plan plan);

    List<Attend> findAllByPlanAndStatus(Plan plan, AttendStatus attendStatus);

    List<Object[]> findMissingAttendEntriesForActiveMembers(LocalDateTime now);
}
