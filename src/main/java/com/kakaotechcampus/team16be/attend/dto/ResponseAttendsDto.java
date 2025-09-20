package com.kakaotechcampus.team16be.attend.dto;

import com.kakaotechcampus.team16be.attend.domain.Attend;
import com.kakaotechcampus.team16be.attend.domain.AttendStatus;

import java.util.List;
import java.util.stream.Collectors;

public record ResponseAttendsDto(String planName, String userName, AttendStatus attendStatus) {

    public static List<ResponseAttendsDto> from(List<Attend> allAttends) {
        return allAttends.stream()
                .map(attend -> new ResponseAttendsDto(
                        attend.getPlan().getTitle(),
                        attend.getGroupMember().getUser().getNickname(),
                        attend.getAttendStatus()
                ))
                .collect(Collectors.toList());
    }
}