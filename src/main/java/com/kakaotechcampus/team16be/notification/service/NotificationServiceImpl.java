package com.kakaotechcampus.team16be.notification.service;

import static com.kakaotechcampus.team16be.notification.domain.NotificationType.CHANGE_GROUP_PLAN;
import static com.kakaotechcampus.team16be.notification.domain.NotificationType.GROUP_JOIN_BANNED;
import static com.kakaotechcampus.team16be.notification.domain.NotificationType.GROUP_JOIN_REJECT;
import static com.kakaotechcampus.team16be.notification.domain.NotificationType.GROUP_JOIN_REQUEST;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.notification.domain.Notification;
import com.kakaotechcampus.team16be.notification.dto.ResponseNotification;
import com.kakaotechcampus.team16be.notification.exception.NotificationErrorCode;
import com.kakaotechcampus.team16be.notification.exception.NotificationException;
import com.kakaotechcampus.team16be.notification.repository.EmitterRepository;
import com.kakaotechcampus.team16be.notification.repository.NotificationRepository;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.review.memberReview.domain.MemberReview;
import com.kakaotechcampus.team16be.review.memberReview.service.MemberReviewService;
import com.kakaotechcampus.team16be.user.domain.User;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final static Long DEFAULT_TIMEOUT = 3600000L;
    private final static String NOTIFICATION_NAME = "notify";

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final MemberReviewService memberReviewService;

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

    public void send(User user, Long notificationId, String message) {
        emitterRepository.get(user.getId()).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event()
                        .id(notificationId.toString())
                        .name(NOTIFICATION_NAME)
                        .data(message));
            } catch (IOException exception) {
                emitterRepository.delete(user.getId());
                throw new NotificationException(NotificationErrorCode.NOTIFICATION_CONNECTION_ERROR);
            }
        }, () -> log.info("No emitter found"));
    }

    @Override
    @Transactional
    public void createGroupSignNotification(User targetUser, Group targetGroup) {
        Notification notification = Notification.builder()
                .notificationType(GROUP_JOIN_REQUEST)
                .receiver(targetGroup.getLeader())
                .relatedGroup(targetGroup)
                .relatedUser(targetUser)
                .nickname(targetUser.getNickname())
                .message("[" + targetGroup.getName() + "] 모임에 " + targetUser.getNickname() + "님이 가입을 요청했습니다.")
                .build();

        notificationRepository.save(notification);

        send(targetGroup.getLeader(), notification.getId(), notification.getMessage());
    }

    @Override
    @Transactional
    public void createGroupJoinNotification(User joiner, Group targetGroup) {
        Notification notification = Notification.builder()
                .notificationType(GROUP_JOIN_REQUEST)
                .receiver(joiner)
                .relatedGroup(targetGroup)
                .relatedUser(targetGroup.getLeader())
                .nickname(joiner.getNickname())
                .message("[" + targetGroup.getName() + "] 모임에 가입되었습니다.")
                .build();

        notificationRepository.save(notification);

        send(joiner, notification.getId(), notification.getMessage());
    }

    @Override
    @Transactional
    public void createGroupLeaveNotification(User leftUser, Group group) {

        System.out.println("createGroupLeaveNotification called");
        List<MemberReview> memberReview = memberReviewService.findByReviewByGroupAndReviewee(group, leftUser);
        boolean checkReview = false;
        if (!memberReview.isEmpty()) {
            checkReview = true;
        }
        String message = "[" + group.getName() + "]모임에서 " + leftUser.getNickname() + "님이 탈퇴했습니다..";
        Notification notification = Notification.createReviewNotification(group.getLeader(), checkReview, group,
                leftUser, message);

        notificationRepository.save(notification);

        send(group.getLeader(), notification.getId(), notification.getMessage());
    }

    @Override
    @Transactional
    public void createPlanUpdateNotifications(Plan plan, List<GroupMember> members) {
        for (GroupMember member : members) {
            Notification notification = Notification.builder()
                    .notificationType(CHANGE_GROUP_PLAN)
                    .receiver(member.getUser())
                    .relatedGroup(plan.getGroup())
                    .relatedUser(plan.getGroup().getLeader())
                    .nickname(member.getUser().getNickname())
                    .message("[" + plan.getGroup().getName() + "] 모임의 일정이 변경되었습니다.")
                    .build();

            notificationRepository.save(notification);
            send(member.getUser(), notification.getId(), notification.getMessage());
        }
    }

    @Override
    @Transactional
    public void createGroupBannedNotification(User bannedUser, Group group) {
        Notification notification = Notification.builder()
                .notificationType(GROUP_JOIN_BANNED)
                .receiver(bannedUser)
                .relatedGroup(group)
                .relatedUser(group.getLeader())
                .nickname(bannedUser.getNickname())
                .message("[" + group.getName() + "] 모임에서 강퇴되었습니다.")
                .build();

        notificationRepository.save(notification);

        send(bannedUser, notification.getId(), notification.getMessage());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseNotification> getAllNotifications(User user) {
        List<Notification> notifications = notificationRepository.findAllByReceiverOrderByCreatedAtDesc((user));

        for (Notification notification : notifications) {
            if (!memberReviewService.findByReviewByGroupAndReviewee(notification.getRelatedGroup(),
                    notification.getRelatedUser()).isEmpty()) {
                notification.markAsReviewed();
            }
        }
        return notifications.stream()
                .map(ResponseNotification::from)
                .toList();
    }

    @Override
    @Transactional
    public void createGroupRejectNotification(User joinUser, Group targetGroup) {
        Notification notification = Notification.builder()
                .notificationType(GROUP_JOIN_REJECT)
                .receiver(joinUser)
                .relatedGroup(targetGroup)
                .relatedUser(targetGroup.getLeader())
                .nickname(joinUser.getNickname())
                .message("[" + targetGroup.getName() + "] 모임의 가입 요청이 거절되었습니다.")
                .build();

        notificationRepository.save(notification);

        send(joinUser, notification.getId(), notification.getMessage());
    }
}
