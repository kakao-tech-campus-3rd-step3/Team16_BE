package com.kakaotechcampus.team16be.group.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateGroupDto(
        @Size(min = 2, max = 8, message = "모임 이름은 2자 이상 8자 이하입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "특수문자를 사용할 수 없습니다.")
        String name,

        String intro,

        @Min(value = 1, message = "모임 최소 인원 수는 1명입니다.")
        Integer capacity,

        String coverImageUrl

) {

}