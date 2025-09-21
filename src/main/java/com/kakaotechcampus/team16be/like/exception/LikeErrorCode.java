package com.kakaotechcampus.team16be.like.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LikeErrorCode {
    ALREADY_LIKED_POST(HttpStatus.BAD_REQUEST, "LIKE_01", "이미 좋아요를 눌렀습니다."),
    NOT_LIKED_POST(HttpStatus.BAD_REQUEST, "LIKE_02", "좋아요를 누르지 않은 게시물입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
