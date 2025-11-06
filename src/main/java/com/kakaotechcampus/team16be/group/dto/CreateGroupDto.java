package com.kakaotechcampus.team16be.group.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record CreateGroupDto(
        @NotBlank(message = "이름은 필수 입력 항목입니다.")
        @Size(min = 2, max = 10, message = "모임 이름은 2자 이상 10자 이하입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "특수문자를 사용할 수 없습니다.")
        String name,

        @NotBlank
        @Size(max = 200, message = "소개는 200자 이하로 작성해야 합니다.")
        String intro,

        @NotNull
        @Min(value = 1, message = "모임 최소 인원 수는 1명입니다.")
        Integer capacity,

        String fileName
) {

}
