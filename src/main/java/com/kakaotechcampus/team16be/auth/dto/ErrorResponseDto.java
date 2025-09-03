package com.kakaotechcampus.team16be.auth.dto;

public class ErrorResponseDto {
    private final String message;
    private final int status;  // HTTP 상태 코드

    public ErrorResponseDto(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

}
