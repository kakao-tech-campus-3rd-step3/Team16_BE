package com.kakaotechcampus.team16be.notification.dto;

import com.kakaotechcampus.team16be.notification.domain.Notification;
import com.kakaotechcampus.team16be.notification.domain.NotificationType;

public record ResponseNotification(
        Long alarmId,
        Long receiverId,
        NotificationType notificationType,
        Long relatedGroupId,
        Long relatedUserId,
        String nickname,
        String message,
        Boolean isReviewed,
        Boolean isRead

) {

    public static ResponseNotification from(Notification notification
    ) {
        return new ResponseNotification(notification.getId(),
                notification.getReceiver().getId(),
                notification.getNotificationType(),
                notification.getRelatedGroup().getId(),
                notification.getRelatedUser().getId(),
                notification.getNickname(),
                notification.getMessage(),
                notification.isReviewed(),
                notification.isRead());
    }
}
