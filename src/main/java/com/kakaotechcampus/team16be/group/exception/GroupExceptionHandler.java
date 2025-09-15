package com.kakaotechcampus.team16be.group.exception;

import com.kakaotechcampus.team16be.group.dto.ResponseGroupDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GroupExceptionHandler {

    @ExceptionHandler(GroupException.class)
    public ResponseEntity<ResponseGroupDto> GroupExceptionHandler(GroupException e) {
        GroupErrorCode groupErrorCode = e.getGroupErrorCode();
        return ResponseEntity.status(groupErrorCode.getStatus()).body(ResponseGroupDto.error(groupErrorCode));
    }

}