package com.kakaotechcampus.team16be.chatroom.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatRoomErrorCode {

    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CR-001", "채팅방을 찾을 수 없습니다."),
    CHATROOM_GROUP_MISMATCH(HttpStatus.BAD_REQUEST, "CR-002", "해당 그룹의 채팅방이 아닙니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
