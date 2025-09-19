package com.kakaotechcampus.team16be.review.common.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import com.kakaotechcampus.team16be.review.common.exception.ReviewErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseReviewDto {

    private final int status;
    private final String code;
    private final String message;

    public static ResponseReviewDto success(HttpStatus status, String message) {
        return new ResponseReviewDto(status.value(), "", message);
    }

    public static ResponseReviewDto error(ReviewErrorCode reviewErrorCode) {
        return new ResponseReviewDto(reviewErrorCode.getStatus().value(), reviewErrorCode.getCode(), reviewErrorCode.getMessage());
    }

    private ResponseReviewDto(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}