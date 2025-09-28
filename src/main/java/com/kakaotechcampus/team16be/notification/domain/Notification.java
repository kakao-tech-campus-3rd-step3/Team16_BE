package com.kakaotechcampus.team16be.notification.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="related_group_id",nullable = false)
    private Group relatedGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_user_id", nullable = false)
    private User relatedUser;

    private String message;

    private Boolean isRead = false;

    @Builder
    public Notification(User receiver, NotificationType notificationType, Group relatedGroup, User relatedUser, String message) {
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.relatedGroup = relatedGroup;
        this.relatedUser = relatedUser;
        this.message = message;
    }

    public static Notification createNotification(User receiver, Notification notificationType, Group relatedGroup, User relatedUser) {
        return Notification.builder()
                .receiver(receiver)
                .notificationType(notificationType.notificationType)
                .relatedGroup(relatedGroup)
                .relatedUser(relatedUser)
                .message(notificationType.getMessage())
                .build();
    }

}
