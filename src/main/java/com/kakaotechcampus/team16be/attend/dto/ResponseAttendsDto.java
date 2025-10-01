package com.kakaotechcampus.team16be.attend.dto;

import com.kakaotechcampus.team16be.attend.domain.Attend;
import com.kakaotechcampus.team16be.attend.domain.AttendStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ResponseAttendsDto(
        String userName,
        @Schema(
        description = "출석 상태 (PRESENT: 출석, LATE: 지각, ABSENT: 결석)",
        allowableValues = {"PRESENT", "LATE", "ABSENT"}) AttendStatus attendStatus,
        LocalDateTime attendAt) {



    public static List<ResponseAttendsDto> from(List<Attend> allAttends) {
        return allAttends.stream()
                .map(attend -> new ResponseAttendsDto(
                        attend.getGroupMember().getUser().getNickname(),
                        attend.getAttendStatus(),
                        attend.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public static ResponseAttendsDto from(Attend attend) {
        return new ResponseAttendsDto(
                attend.getGroupMember().getUser().getNickname(),
                attend.getAttendStatus(),
                attend.getCreatedAt()
        );
    }
}