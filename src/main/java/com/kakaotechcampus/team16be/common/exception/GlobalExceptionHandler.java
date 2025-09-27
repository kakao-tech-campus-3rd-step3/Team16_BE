package com.kakaotechcampus.team16be.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponseDto> handleBaseException(BaseException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponseDto(e.getMessage(), e.getCode(), e.getStatus()));
    }
}
