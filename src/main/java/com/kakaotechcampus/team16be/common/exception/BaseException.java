package com.kakaotechcampus.team16be.common.exception;

public abstract class BaseException extends RuntimeException {

    public abstract String getMessage();

    public abstract String getCode();

    public abstract int getStatus();
}
