package com.kakaotechcampus.team16be.group.exception;

import com.kakaotechcampus.team16be.common.exception.BaseException;
import lombok.Getter;

@Getter
public class GroupException extends BaseException {

    private final GroupErrorCode errorCode;

    public GroupException(GroupErrorCode errorCode) {
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