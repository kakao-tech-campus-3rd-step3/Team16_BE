package com.kakaotechcampus.team16be.common.exception;

import com.kakaotechcampus.team16be.auth.exception.JwtException;
import com.kakaotechcampus.team16be.auth.exception.KakaoException;
import com.kakaotechcampus.team16be.groundrule.exception.GroundRuleException;
import com.kakaotechcampus.team16be.group.exception.GroupException;
import com.kakaotechcampus.team16be.report.exception.ReportException;
import com.kakaotechcampus.team16be.user.exception.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // JWT 관련 예외
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponseDto> handleJwtException(JwtException e) {
        return buildErrorResponse(e.getErrorCode().getMessage(),
                e.getErrorCode().getCode(),
                e.getErrorCode().getStatus().value());
    }

    // Kakao 관련 예외
    @ExceptionHandler(KakaoException.class)
    public ResponseEntity<ErrorResponseDto> handleKakaoException(KakaoException e) {
        return buildErrorResponse(e.getErrorCode().getMessage(),
                e.getErrorCode().getCode(),
                e.getErrorCode().getStatus().value());
    }

    // User 예외
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponseDto> handleUserException(UserException e) {
        return buildErrorResponse(e.getErrorCode().getMessage(),
                e.getErrorCode().getCode(),
                e.getErrorCode().getStatus().value());
    }

    // Report 예외
    @ExceptionHandler(ReportException.class)
    public ResponseEntity<ErrorResponseDto> handleReportException(ReportException e) {
        return buildErrorResponse(e.getReportErrorCode().getMessage(),
                e.getReportErrorCode().getCode(),
                e.getReportErrorCode().getStatus().value());
    }

    // Group 예외
    @ExceptionHandler(GroupException.class)
    public ResponseEntity<ErrorResponseDto> handleGroupException(GroupException e) {
        return buildErrorResponse(e.getGroupErrorCode().getMessage(),
                e.getGroupErrorCode().getCode(),
                e.getGroupErrorCode().getStatus().value());
    }

    // GroundRule 예외
    @ExceptionHandler(GroundRuleException.class)
    public ResponseEntity<ErrorResponseDto> handleGroundRuleException(GroundRuleException e) {
        return buildErrorResponse(e.getErrorCode().getMessage(),
                e.getErrorCode().getCode(),
                e.getErrorCode().getStatus().value());
    }

    private ResponseEntity<ErrorResponseDto> buildErrorResponse(String message, String code, int status) {
        return ResponseEntity.status(status)
                .body(new ErrorResponseDto(message, code, status));
    }
}
