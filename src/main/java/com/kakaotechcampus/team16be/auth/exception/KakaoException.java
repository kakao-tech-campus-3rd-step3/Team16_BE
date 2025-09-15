package com.kakaotechcampus.team16be.auth.exception;

import lombok.Getter;

@Getter
public class KakaoException extends RuntimeException {

    private final KakaoErrorCode errorCode;

    public KakaoException(KakaoErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
