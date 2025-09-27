package com.kakaotechcampus.team16be.plan.exception;

import com.kakaotechcampus.team16be.common.exception.BaseException;
import lombok.Getter;


@Getter
public class PlanException extends BaseException {

    private final PlanErrorCode errorCode;

    public PlanException(PlanErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    @Override
    public String getCode() {
        return errorCode.getCode();
    }

    @Override
    public int getStatus() {
        return errorCode.getStatus().value();
    }
}
