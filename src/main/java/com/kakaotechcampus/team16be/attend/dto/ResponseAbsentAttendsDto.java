package com.kakaotechcampus.team16be.attend.dto;

import com.kakaotechcampus.team16be.attend.domain.Attend;

import java.util.List;

public record ResponseAbsentAttendsDto(Long userId, String nickname) {

    public static List<ResponseAbsentAttendsDto> from(List<Attend> attends) {
        return attends.stream()
                .map(attend -> new ResponseAbsentAttendsDto(
                        attend.getGroupMember().getUser().getId(),
                        attend.getGroupMember().getUser().getNickname()
                ))
                .toList();
    }

}
