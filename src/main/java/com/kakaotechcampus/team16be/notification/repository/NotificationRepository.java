package com.kakaotechcampus.team16be.notification.repository;

import com.kakaotechcampus.team16be.notification.domain.Notification;
import com.kakaotechcampus.team16be.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiverOrderByCreatedAtDesc(User receiver);

    void deleteAllByReceiverId(Long receiverId);

    void deleteAllByRelatedUserId(Long relatedUserId);
}
