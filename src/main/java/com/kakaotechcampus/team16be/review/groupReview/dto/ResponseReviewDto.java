package com.kakaotechcampus.team16be.review.groupReview.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.kakaotechcampus.team16be.group.exception.ErrorCode;
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

    public static ResponseReviewDto error(ErrorCode errorCode) {
        return new ResponseReviewDto(errorCode.getStatus().value(), errorCode.getCode(), errorCode.getMessage());
    }

    private ResponseReviewDto(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}