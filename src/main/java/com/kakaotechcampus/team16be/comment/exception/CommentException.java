package com.kakaotechcampus.team16be.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentException extends RuntimeException {
    private final CommentErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
