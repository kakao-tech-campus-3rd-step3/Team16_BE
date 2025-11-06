package com.kakaotechcampus.team16be.notification.controller;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.notification.dto.ResponseNotification;
import com.kakaotechcampus.team16be.notification.service.NotificationService;
import com.kakaotechcampus.team16be.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
@Tag(name = "알림 API", description = "알림 관련 API")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(
            summary = "알림 구독",
            description = "사용자가 SSE(Server-Sent Events) 방식으로 실시간 알림을 구독합니다. " +
                    "이 API를 호출하면 서버와의 연결이 유지되며, 새로운 알림이 발생할 때마다 클라이언트로 이벤트가 전송됩니다."
    )
    @GetMapping("/subscribe")
    public SseEmitter subscribe(@LoginUser User user) {
        return notificationService.connectNotification(user);
    }

    @Operation(
            summary = "전체 알림 조회",
            description = "로그인한 사용자가 받은 모든 알림을 조회합니다."
    )
    @GetMapping
    public ResponseEntity<List<ResponseNotification>> getAllNotifications(@LoginUser User user) {
        List<ResponseNotification> notifications = notificationService.getAllNotifications(user);
        return ResponseEntity.ok(notifications);
    }
}
