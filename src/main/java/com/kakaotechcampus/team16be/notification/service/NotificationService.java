package com.kakaotechcampus.team16be.notification.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.user.domain.User;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    SseEmitter connectNotification(User user);

    void createGroupSignNotification(User targetUser, Group targetGroup);

    void createGroupJoinNotification(User joiner, Group targetGroup);

    void createGroupLeaveNotification(User user, Group group);
}
