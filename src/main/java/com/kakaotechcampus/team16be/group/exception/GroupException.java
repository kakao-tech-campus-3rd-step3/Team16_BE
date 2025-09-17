package com.kakaotechcampus.team16be.group.exception;

import lombok.Getter;

@Getter
public class GroupException extends RuntimeException {

    private final GroupErrorCode groupErrorCode;

    public GroupException(GroupErrorCode groupErrorCode) {
        super(groupErrorCode.getMessage());
        this.groupErrorCode = groupErrorCode;
    }
}