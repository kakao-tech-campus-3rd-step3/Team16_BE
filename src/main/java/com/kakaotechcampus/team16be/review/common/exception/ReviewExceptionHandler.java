package com.kakaotechcampus.team16be.review.common.exception;

import com.kakaotechcampus.team16be.review.common.dto.ResponseReviewDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReviewExceptionHandler {

    @ExceptionHandler(ReviewException.class)
    public ResponseEntity<ResponseReviewDto> ReviewExceptionHandler(ReviewException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(ResponseReviewDto.error(errorCode));
    }

}