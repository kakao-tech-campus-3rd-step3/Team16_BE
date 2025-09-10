package com.kakaotechcampus.team16be.group.dto;

import jakarta.validation.constraints.*;


public record CreateGroupDto(
        @NotBlank(message = "이름은 필수 입력 항목입니다.")
        @Size(min = 2, max = 8, message = "모임 이름은 2자 이상 8자 이하입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "특수문자를 사용할 수 없습니다.")
        String name,


        @NotBlank
        String intro,

        @NotNull
        @Min(value = 1, message = "모임 최소 인원 수는 1명입니다.")
        Integer capacity
) {

}