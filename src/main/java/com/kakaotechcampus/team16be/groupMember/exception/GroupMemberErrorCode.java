package com.kakaotechcampus.team16be.groupMember.exception;

import com.kakaotechcampus.team16be.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum
GroupMemberErrorCode implements ErrorCode {

    GROUP_MEMBER_ALREADY_EXIST(HttpStatus.CONFLICT, "GROUP_MEMBER-001", "이미 그룹에 속해있는 멤버입니다."),
    GROUP_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "GROUP_MEMBER-002", "해당 유저는 그룹의 멤버가 아닙니다."),
    LEADER_CANNOT_JOIN(HttpStatus.BAD_REQUEST, "GROUP_MEMBER-003", "그룹장은 그룹에 멤버로 가입할 수 없습니다."),
    LEADER_CANNOT_LEAVE(HttpStatus.BAD_REQUEST, "GROUP_MEMBER-004", "그룹장은 그룹을 탈퇴할 수 없습니다."),
    MEMBER_ALREADY_LEFT(HttpStatus.BAD_REQUEST, "GROUP_MEMBER-005", "이미 탈퇴한 멤버입니다."),
    MEMBER_HAS_BANNED(HttpStatus.BAD_REQUEST, "GROUP_MEMBER-006", "강퇴당한 멤버는 가입할 수 없습니다."),
    FAILED_TO_JOIN_GROUP(HttpStatus.BAD_REQUEST, "GROUP_MEMBER-007", "알 수 없는 이유로 그룹에 가입하지 못했습니다."),
    MEMBER_ALREADY_BANNED(HttpStatus.BAD_REQUEST, "GROUP_MEMBER-008", "해당 유저는 이미 강퇴 당한 유저입니다."),
    MEMBER_CANNOT_CANCEL(HttpStatus.BAD_REQUEST, "GROUP_MEMBER-009", "해당 유저는 가입 신청 취소할 수 없는 상태입니다."),
    MEMBER_NOT_PENDING(HttpStatus.BAD_REQUEST, "GROUP_MEMBER-010", "해당 유저는 가입 승인할 수 없는 상태입니다."),
    MEMBER_CANNOT_REJECT(HttpStatus.BAD_REQUEST, "GROUP_MEMBER-011", "해당 유저는 가입 거절 할 수 없는 상태입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
