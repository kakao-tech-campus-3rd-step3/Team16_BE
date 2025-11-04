package com.kakaotechcampus.team16be.plan.scheduler;

import com.kakaotechcampus.team16be.attend.domain.Attend;
import com.kakaotechcampus.team16be.attend.repository.AttendRepository;
import com.kakaotechcampus.team16be.attend.service.AttendService;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.plan.PlanRepository;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.plan.service.PlanService;
import com.kakaotechcampus.team16be.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlanScheduler {

    private final PlanService planService;
    private final AttendService attendService;
    private final GroupMemberService groupMemberService;
    private final UserService userService;

    @Scheduled(cron = "0 */1 * * * *")
    @Transactional
    public void ReflectAbsentAttendees() {

        LocalDateTime now = LocalDateTime.now();
        List<Plan> endedPlans = planService.findAllByEndTimeBetween(now.minusMinutes(5), now);


        for (Plan plan : endedPlans) {

            List<GroupMember> allMembers= groupMemberService.getActiveMember(plan.getGroup());

            Set<Long> attendedMemberIds = attendService.findAllByPlan(plan).stream()
                    .map(attend -> attend.getGroupMember().getId())
                    .collect(Collectors.toSet());

            List<Attend> absentAttendees = allMembers.stream()
                    .filter(member -> !attendedMemberIds.contains(member.getId()))
                    .map(absentMember -> Attend.absentPlan(absentMember, plan))
                    .toList();

            if (!absentAttendees.isEmpty()) {
                for (Attend absentAttendee : absentAttendees) {
                    userService.decreaseScoreByAbsent(absentAttendee.getGroupMember().getUser());
                }
                attendService.saveAll(absentAttendees);
            }
        }
    }
}
