package com.kakaotechcampus.team16be.attend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GetAttendeesResponse(List<ResponseAttendsDto> attendees, boolean isUserAttended) {


    public static GetAttendeesResponse from(List<ResponseAttendsDto> attendsDtos, boolean isUserAttended) {
        return new GetAttendeesResponse(attendsDtos, isUserAttended);
    }
}
