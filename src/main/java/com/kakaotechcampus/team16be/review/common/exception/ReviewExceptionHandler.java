package com.kakaotechcampus.team16be.review.common.exception;

import com.kakaotechcampus.team16be.group.dto.ResponseGroupDto;
import com.kakaotechcampus.team16be.group.exception.ErrorCode;
import com.kakaotechcampus.team16be.group.exception.GroupException;
import com.kakaotechcampus.team16be.groupMember.exception.GroupMemberException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReviewExceptionHandler {

    @ExceptionHandler(GroupMemberException.class)
    public ResponseEntity<ResponseGroupDto> GroupMemberExceptionHandler(GroupException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(ResponseGroupDto.error(errorCode));
    }

}