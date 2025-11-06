package com.kakaotechcampus.team16be.group.scheduler;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GroupScheduler {

    private static final Double BASE_GROUP_SCORE = 80.0;
    private static final Double BASE_USER_SCORE = 40.0;
    private static final Double MEMBER_SCORE_WEIGHT = 0.4;
    private final GroupService groupService;
    private final GroupMemberService groupMemberService;

    @Scheduled(cron = "* 30 * * * *")
    @Transactional
    public void reflectGroupScore() {
        List<Group> allGroups = groupService.findAll();

        for (Group group : allGroups) {
            groupService.updateGroupScore(group, calculateScoreByMembers(group));
            groupService.updateGroupTag(group);
        }
    }

    private Double calculateScoreByMembers(Group group) {

        List<GroupMember> activeMembers = groupMemberService.getActiveMember(group);

        if (activeMembers.isEmpty()) {
            return BASE_GROUP_SCORE;
        }

        double avgMemberScore = activeMembers.stream()
                .mapToDouble(member -> member.getUser().getScore())
                .average()
                .orElse(BASE_USER_SCORE);

        double scoreDifference = avgMemberScore - (BASE_USER_SCORE+35);

        double calculatedScore = BASE_GROUP_SCORE + (scoreDifference * MEMBER_SCORE_WEIGHT);

        return Math.max(0, Math.min(100, calculatedScore));
    }
}
