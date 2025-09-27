package com.kakaotechcampus.team16be.user.exception;

import com.kakaotechcampus.team16be.common.exception.BaseException;
import lombok.Getter;

@Getter
public class UserException extends BaseException {

    private final UserErrorCode errorCode;

    public UserException(UserErrorCode errorCode) {
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
