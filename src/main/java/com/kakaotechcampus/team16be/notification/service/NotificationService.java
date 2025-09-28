package com.kakaotechcampus.team16be.notification.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.user.domain.User;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface NotificationService {

    SseEmitter connectNotification(User user);

    void createGroupSignNotification(User targetUser, Group targetGroup);

    void createGroupJoinNotification(User joiner, Group targetGroup);

    void createGroupLeaveNotification(User user, Group group);

    void createPlanUpdateNotifications(Plan plan, List<GroupMember> members);

    void createGroupBannedNotification(User bannedUser, Group group);
}
