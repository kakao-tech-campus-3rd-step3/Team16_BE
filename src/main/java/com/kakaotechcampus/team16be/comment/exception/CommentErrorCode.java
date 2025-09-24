package com.kakaotechcampus.team16be.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode {
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMMENT-001", "해당 댓글을 찾을 수 없습니다."),
    COMMENT_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "COMMENT-002", "댓글에 대한 권한이 없습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
