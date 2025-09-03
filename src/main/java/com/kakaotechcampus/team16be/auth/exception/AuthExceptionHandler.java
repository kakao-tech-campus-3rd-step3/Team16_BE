package com.kakaotechcampus.team16be.auth.exception;

import com.kakaotechcampus.team16be.auth.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponseDto> handleJwtException(JwtException e) {
        return buildErrorResponse(
                e.getErrorCode().getMessage(),
                e.getErrorCode().getHttpStatus().value()
        );
    }

    @ExceptionHandler(KakaoException.class)
    public ResponseEntity<ErrorResponseDto> handleKakaoException(KakaoException e) {
        return buildErrorResponse(
                e.getErrorCode().getMessage(),
                e.getErrorCode().getHttpStatus().value()
        );
    }

    private ResponseEntity<ErrorResponseDto> buildErrorResponse(String message, int statusCode) {
        return ResponseEntity
                .status(statusCode)
                .body(new ErrorResponseDto(message, statusCode));
    }
}