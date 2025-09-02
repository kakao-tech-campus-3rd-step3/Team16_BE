package com.kakaotechcampus.team16be.group.exception;

import lombok.Getter;

@Getter
public class GroupException extends RuntimeException {

    private final ErrorCode errorCode;

    public GroupException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}