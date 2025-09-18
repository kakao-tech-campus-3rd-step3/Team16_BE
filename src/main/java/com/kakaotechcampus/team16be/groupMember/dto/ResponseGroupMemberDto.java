package com.kakaotechcampus.team16be.groupMember.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kakaotechcampus.team16be.groupMember.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseGroupMemberDto {

    private final int status;
    private final String code;
    private final String message;

    public static ResponseGroupMemberDto success(HttpStatus status, String message) {
        return new ResponseGroupMemberDto(status.value(), "", message);
    }

    public static ResponseGroupMemberDto error(ErrorCode errorCode) {
        return new ResponseGroupMemberDto(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage());
    }

    private ResponseGroupMemberDto(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}