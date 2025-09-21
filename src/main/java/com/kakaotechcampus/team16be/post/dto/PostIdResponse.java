package com.kakaotechcampus.team16be.post.dto;

public record PostIdResponse (
        Long postId
) {
    public static PostIdResponse from(Long postId) {
        return new PostIdResponse(postId);
    }
}
