package com.kakaotechcampus.team16be.attend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GetAttendeesResponse(List<ResponseAttendsDto> attendees, boolean isUserAttended) {

    @Schema(
            description = "출석 상태 (PRESENT: 출석, LATE: 지각, ABSENT: 결석)",
            allowableValues = {"PRESENT", "LATE", "ABSENT"}
    )
    public static GetAttendeesResponse from(List<ResponseAttendsDto> attendsDtos, boolean isUserAttended) {
        return new GetAttendeesResponse(attendsDtos, isUserAttended);
    }
}
