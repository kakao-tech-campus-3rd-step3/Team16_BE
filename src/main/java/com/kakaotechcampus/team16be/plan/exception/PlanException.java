package com.kakaotechcampus.team16be.plan.exception;

import lombok.Getter;

@Getter
public class PlanException extends RuntimeException {

    private final PlanErrorCode planErrorCode;

    public PlanException(PlanErrorCode planErrorCode) {
        super(planErrorCode.getMessage());
        this.planErrorCode = planErrorCode;
    }
}
