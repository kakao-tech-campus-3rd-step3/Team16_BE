package com.kakaotechcampus.team16be.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String getMessage();

    String getCode();

    HttpStatus getStatus();
}
