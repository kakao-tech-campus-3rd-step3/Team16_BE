package com.kakaotechcampus.team16be.notification.repository;

import com.kakaotechcampus.team16be.notification.domain.Notification;
import com.kakaotechcampus.team16be.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiverOrderByCreatedAtDesc(User receiver);
}
