package com.kakaotechcampus.team16be.groupMember.exception;


import lombok.Getter;

@Getter
public class GroupMemberException extends RuntimeException {

    private final GroupMemberErrorCode errorCode;

    public GroupMemberException(GroupMemberErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}