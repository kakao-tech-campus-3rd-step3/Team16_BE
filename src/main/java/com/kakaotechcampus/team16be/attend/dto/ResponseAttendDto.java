package com.kakaotechcampus.team16be.attend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kakaotechcampus.team16be.groupMember.exception.GroupMemberErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseAttendDto {

    private final int status;
    private final String code;
    private final String message;

    public static ResponseAttendDto success(HttpStatus status, String message) {
        return new ResponseAttendDto(status.value(), "", message);
    }

    public static ResponseAttendDto error(GroupMemberErrorCode errorCode) {
        return new ResponseAttendDto(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage());
    }

    private ResponseAttendDto(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}