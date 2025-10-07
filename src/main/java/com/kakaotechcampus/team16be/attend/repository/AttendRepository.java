package com.kakaotechcampus.team16be.attend.repository;

import com.kakaotechcampus.team16be.attend.domain.Attend;
import com.kakaotechcampus.team16be.attend.domain.AttendStatus;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AttendRepository extends JpaRepository<Attend, Long> {

    List<Attend> findAllByPlan(Plan plan);

    Attend findByPlanAndGroupMember(Plan plan, GroupMember groupMember);

    List<Attend> findAllByPlanAndGroupMemberNotIn(Plan plan, Collection<GroupMember> groupMembers);

    List<Attend> findAllByGroupMemberOrderByCreatedAtAsc(GroupMember groupMember);

    List<Attend> findAllByPlanOrderByCreatedAtAsc(Plan plan);

    List<Attend> findAllByPlanAndAttendStatus(Plan plan, AttendStatus attendStatus);
}
