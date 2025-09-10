package com.kakaotechcampus.team16be.user.dto;

public record UserNicknameResponse(
        String nickname
) {
    public static UserNicknameResponse from(String nickname) {
        return new UserNicknameResponse(nickname);
    }
}
