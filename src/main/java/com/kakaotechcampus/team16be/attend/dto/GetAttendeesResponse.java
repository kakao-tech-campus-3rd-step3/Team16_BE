package com.kakaotechcampus.team16be.attend.dto;

import java.util.List;

public record GetAttendeesResponse(List<ResponseAttendsDto> attendees, boolean isUserAttended) {


    public static GetAttendeesResponse from(List<ResponseAttendsDto> attendsDtos, boolean isUserAttended) {
        return new GetAttendeesResponse(attendsDtos, isUserAttended);
    }
}
