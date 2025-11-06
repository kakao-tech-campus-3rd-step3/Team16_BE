package com.kakaotechcampus.team16be.like.dto;

public record PostLikeResponse(
        Long likeCount,
        Boolean isLiked
) {

}
