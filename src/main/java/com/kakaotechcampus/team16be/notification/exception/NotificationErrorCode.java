package com.kakaotechcampus.team16be.notification.exception;

import com.kakaotechcampus.team16be.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationErrorCode implements ErrorCode {

    NOTIFICATION_CONNECTION_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "NOTIFICATION-001", "알림 SSE 연결 중 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
