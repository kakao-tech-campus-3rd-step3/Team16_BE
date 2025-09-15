package com.kakaotechcampus.team16be.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private final String message;
    private final String code;
    private final int status;
}
