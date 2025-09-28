package com.kakaotechcampus.team16be.notification.exception;

import com.kakaotechcampus.team16be.common.exception.BaseException;

public class NotificationException extends BaseException {

    private final NotificationErrorCode errorCode;

    public NotificationException(NotificationErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    @Override
    public String getCode() {
        return errorCode.getCode();
    }

    @Override
    public int getStatus() {
        return errorCode.getStatus().value();
    }
}
