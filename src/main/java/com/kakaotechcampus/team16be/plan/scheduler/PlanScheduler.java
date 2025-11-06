package com.kakaotechcampus.team16be.plan.scheduler;

import com.kakaotechcampus.team16be.attend.domain.Attend;
import com.kakaotechcampus.team16be.attend.domain.AttendStatus;
import com.kakaotechcampus.team16be.attend.service.AttendService;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.plan.service.PlanService;
import com.kakaotechcampus.team16be.planParticipant.PlanParticipantRepository;
import com.kakaotechcampus.team16be.planParticipant.domain.ParticipantStatus;
import com.kakaotechcampus.team16be.planParticipant.domain.PlanParticipant;
import com.kakaotechcampus.team16be.planParticipant.service.PlanParticipantService;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.service.UserService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
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
    private final GroupMemberService groupMemberService; // (수정) 이 로직에서는 필요 없어짐
    private final UserService userService;
    private final PlanParticipantRepository planParticipantRepository; // (수F정) 이 로직에서는 필요 없어짐
    private final PlanParticipantService planParticipantService;

    // (참고) fixedRate(15초)와 조회 시간(15초)을 일치시켜야 중복 처리가 안됩니다.
    private static final int SCHEDULE_RATE_SECONDS = 15;

    @Scheduled(fixedRate = 15000) // 15초마다 실행
    @Transactional
    public void ReflectAbsentAttendees() {

        System.out.println("실행됨?");
        LocalDateTime now = LocalDateTime.now();

        List<Plan> endedPlans = planService.findAllByEndTimeBetween(now.minusHours(2), now);

        if (!endedPlans.isEmpty()) {
            System.out.println(now + ": 스케줄러 실행. " + endedPlans.size() + "개의 종료된 일정 처리 시작.");
        }

        for (Plan plan : endedPlans) {
            Set<Long> attendingParticipantUserIds = planParticipantService.findAllByPlan(plan).stream()
                    .filter(p -> p.getParticipantStatus() == ParticipantStatus.ATTENDING)
                    .map(participant -> participant.getUser().getId())
                    .collect(Collectors.toSet());

            for (Long attendingParticipantUserId : attendingParticipantUserIds) {
                System.out.println("Plan(" + plan.getId() + ") 참석한 참가자 User ID: " + attendingParticipantUserId);
            }

            Map<Long, Attend> attendMap = attendService.findAllByPlan(plan).stream()
                    .collect(Collectors.toMap(attend -> attend.getGroupMember().getId(), Function.identity()));

            for (Attend value : attendMap.values()) {
                System.out.println(value.getGroupMember().getUser().getId());
            }

            List<Attend> attendsToSaveOrUpdate = new ArrayList<>();
            List<User> usersToPenalize = new ArrayList<>();

            for (Attend existingAttend : attendMap.values()) {

                if (existingAttend.getAttendStatus() == AttendStatus.PENDING) {

                    User memberUser = existingAttend.getGroupMember().getUser();

                    if (attendingParticipantUserIds.contains(memberUser.getId())) {

                        System.out.println("Plan(" + plan.getId() + ") / User(" + memberUser.getId() + "): PENDING -> ABSENT 처리");

                        existingAttend.updateStatus(AttendStatus.ABSENT);
                        attendsToSaveOrUpdate.add(existingAttend);
                        usersToPenalize.add(memberUser);
                    }

                }
            }


            if (!attendsToSaveOrUpdate.isEmpty()) {
                for (User user : usersToPenalize) {
                    userService.decreaseScoreByAbsent(user);
                }
                attendService.saveAll(attendsToSaveOrUpdate);
            }
        }
    }
}