package com.kakaotechcampus.team16be.post.exception;

import com.kakaotechcampus.team16be.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

    POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "POST-001", "해당 게시글을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
