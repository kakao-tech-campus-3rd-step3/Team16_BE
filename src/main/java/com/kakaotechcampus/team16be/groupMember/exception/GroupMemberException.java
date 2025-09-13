package com.kakaotechcampus.team16be.groupMember.exception;


import lombok.Getter;

@Getter
public class GroupMemberException extends RuntimeException {

    private final ErrorCode errorCode;

    public GroupMemberException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}