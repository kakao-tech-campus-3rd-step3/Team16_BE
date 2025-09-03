package com.kakaotechcampus.team16be.auth.exception;

public class KakaoException extends RuntimeException {

    private final KakaoErrorCode errorCode;

    public KakaoException(KakaoErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public KakaoErrorCode getErrorCode() {
        return errorCode;
    }

}
