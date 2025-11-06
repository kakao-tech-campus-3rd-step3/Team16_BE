package com.kakaotechcampus.team16be.plan.scheduler;

import com.kakaotechcampus.team16be.attend.domain.Attend;
import com.kakaotechcampus.team16be.attend.domain.AttendStatus;
import com.kakaotechcampus.team16be.attend.service.AttendService;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.plan.service.PlanService;
import com.kakaotechcampus.team16be.planParticipant.domain.ParticipantStatus;
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
    private final UserService userService;
    private final PlanParticipantService planParticipantService;


    private static final int SCHEDULE_RATE_SECONDS = 60;
    private static final int SCHEDULE_RATE_MILLISECONDS = SCHEDULE_RATE_SECONDS * 1000;

    @Scheduled(fixedRate = SCHEDULE_RATE_MILLISECONDS)
    @Transactional
    public void ReflectAbsentAttendees() {

        LocalDateTime now = LocalDateTime.now();

        List<Plan> endedPlans = planService.findAllByEndTimeBetween(now.minusHours(4), now);

        for (Plan plan : endedPlans) {
            Set<Long> attendingParticipantUserIds = planParticipantService.findAllByPlan(plan).stream()
                    .filter(p -> p.getParticipantStatus() == ParticipantStatus.ATTENDING)
                    .map(participant -> participant.getUser().getId())
                    .collect(Collectors.toSet());

            Map<Long, Attend> attendMap = attendService.findAllByPlan(plan).stream()
                    .collect(Collectors.toMap(attend -> attend.getGroupMember().getId(), Function.identity()));

            List<Attend> attendsToSaveOrUpdate = new ArrayList<>();
            List<User> usersToPenalize = new ArrayList<>();

            for (Attend existingAttend : attendMap.values()) {
                if (existingAttend.getAttendStatus() == AttendStatus.HOLDING) {
                    User memberUser = existingAttend.getGroupMember().getUser();
                    if (attendingParticipantUserIds.contains(memberUser.getId())) {
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