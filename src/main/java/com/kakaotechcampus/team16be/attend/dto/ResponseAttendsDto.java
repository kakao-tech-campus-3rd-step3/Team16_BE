package com.kakaotechcampus.team16be.attend.dto;

import com.kakaotechcampus.team16be.attend.domain.Attend;
import com.kakaotechcampus.team16be.attend.domain.AttendStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ResponseAttendsDto(String planName, String userName, AttendStatus attendStatus, LocalDateTime attendAt) {

    public static List<ResponseAttendsDto> from(List<Attend> allAttends) {
        return allAttends.stream()
                .sorted((a1, a2) -> a1.getCreatedAt().compareTo(a2.getCreatedAt()))
                .map(attend -> new ResponseAttendsDto(
                        attend.getPlan().getTitle(),
                        attend.getGroupMember().getUser().getNickname(),
                        attend.getAttendStatus(),
                        attend.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }


    public static ResponseAttendsDto from(Attend attend) {
        return new ResponseAttendsDto(
                attend.getPlan().getTitle(),
                attend.getGroupMember().getUser().getNickname(),
                attend.getAttendStatus(),
                attend.getCreatedAt()
        );
    }
}