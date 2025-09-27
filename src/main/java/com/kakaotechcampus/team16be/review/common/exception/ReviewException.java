package com.kakaotechcampus.team16be.review.common.exception;

import com.kakaotechcampus.team16be.common.exception.BaseException;
import lombok.Getter;

@Getter
public class ReviewException extends BaseException {

    private final ReviewErrorCode errorCode;

    public ReviewException(ReviewErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    @Override
    public String getCode() {
        return errorCode.getCode();
    }

    @Override
    public int getStatus() {
        return errorCode.getStatus().value();
    }
}
