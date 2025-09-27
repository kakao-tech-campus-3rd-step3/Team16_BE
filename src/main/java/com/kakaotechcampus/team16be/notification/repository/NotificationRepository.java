package com.kakaotechcampus.team16be.notification.repository;

import com.kakaotechcampus.team16be.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
