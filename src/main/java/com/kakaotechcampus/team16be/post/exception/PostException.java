package com.kakaotechcampus.team16be.post.exception;

import com.kakaotechcampus.team16be.common.exception.BaseException;
import lombok.Getter;

@Getter
public class PostException extends BaseException {

    private final PostErrorCode errorCode;

    public PostException(PostErrorCode errorCode) {
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
