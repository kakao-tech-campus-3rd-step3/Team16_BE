package com.kakaotechcampus.team16be.user.exception;

import com.kakaotechcampus.team16be.auth.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponseDto> handleUserException(UserException e) {
        return buildErrorResponse(
                e.getErrorCode().getMessage(),
                e.getErrorCode().getStatus().value()
        );
    }

    private ResponseEntity<ErrorResponseDto> buildErrorResponse(String message, int statusCode) {
        return ResponseEntity
                .status(statusCode)
                .body(new ErrorResponseDto(message, statusCode));
    }
}
