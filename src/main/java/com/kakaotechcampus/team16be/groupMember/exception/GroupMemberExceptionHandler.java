package com.kakaotechcampus.team16be.groupMember.exception;

import com.kakaotechcampus.team16be.groupMember.dto.ResponseGroupMemberDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GroupMemberExceptionHandler {

    @ExceptionHandler(GroupMemberException.class)
    public ResponseEntity<ResponseGroupMemberDto> GroupMemberExceptionHandler(GroupMemberException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(ResponseGroupMemberDto.error(errorCode));
    }

}