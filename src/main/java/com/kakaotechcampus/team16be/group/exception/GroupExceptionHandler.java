package com.kakaotechcampus.team16be.group.exception;

import com.kakaotechcampus.team16be.group.dto.ResponseGroupDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GroupExceptionHandler {

    @ExceptionHandler(GroupException.class)
    public ResponseEntity<ResponseGroupDto> GroupExceptionHandler(GroupException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(ResponseGroupDto.error(errorCode));
    }

}