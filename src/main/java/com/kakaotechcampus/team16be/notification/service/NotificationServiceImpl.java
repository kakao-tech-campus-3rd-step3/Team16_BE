package com.kakaotechcampus.team16be.notification.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.notification.domain.Notification;
import com.kakaotechcampus.team16be.notification.exception.NotificationErrorCode;
import com.kakaotechcampus.team16be.notification.exception.NotificationException;
import com.kakaotechcampus.team16be.notification.repository.EmitterRepository;
import com.kakaotechcampus.team16be.notification.repository.NotificationRepository;
import com.kakaotechcampus.team16be.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;

import static com.kakaotechcampus.team16be.notification.domain.NotificationType.GROUP_JOIN_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final static Long DEFAULT_TIMEOUT = 3600000L;
    private final static String NOTIFICATION_NAME = "notify";

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public SseEmitter connectNotification(User user) {

        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        emitterRepository.save(user.getId(), sseEmitter);

        sseEmitter.onCompletion(() -> emitterRepository.delete(user.getId()));
        sseEmitter.onTimeout(() -> emitterRepository.delete(user.getId()));

        try {
            sseEmitter.send(SseEmitter.event()
                    .id("")
                    .name(NOTIFICATION_NAME)
                    .data("Connection completed"));
        } catch (IOException exception) {
            throw new NotificationException(NotificationErrorCode.NOTIFICATION_CONNECTION_ERROR);
        }
        return sseEmitter;
    }

    public void send(User user, Long notificationId) {
        emitterRepository.get(user.getId()).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event()
                        .id(notificationId.toString())
                        .name(NOTIFICATION_NAME)
                        .data("새로운 알림이 도착했습니다."));
            } catch (IOException exception) {
                emitterRepository.delete(user.getId());
                throw new NotificationException(NotificationErrorCode.NOTIFICATION_CONNECTION_ERROR);
            }
        }, () -> log.info("No emitter found"));
    }

    public void createGroupJoinNotification(User targetUser, Group targetGroup) {
        Notification notification = Notification.builder()
                .notificationType(GROUP_JOIN_REQUEST)
                .receiver(targetGroup.getLeader())
                .relatedGroup(targetGroup)
                .relatedUser(targetUser)
                .message("[" + targetGroup.getName() + "] 그룹에 " + targetUser.getNickname() + "님이 가입을 요청했습니다.")
                .build();

        notificationRepository.save(notification);

        send(targetGroup.getLeader(), notification.getId());
    }

}
