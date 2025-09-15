package com.kakaotechcampus.team16be.group.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kakaotechcampus.team16be.group.exception.GroupErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseGroupDto {

    private final int status;
    private final String code;
    private final String message;

    public static ResponseGroupDto success(HttpStatus status, String message) {
        return new ResponseGroupDto(status.value(), "", message);
    }

    public static ResponseGroupDto error(GroupErrorCode groupErrorCode) {
        return new ResponseGroupDto(groupErrorCode.getStatus().value(), groupErrorCode.getCode(), groupErrorCode.getMessage());
    }

    private ResponseGroupDto(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}