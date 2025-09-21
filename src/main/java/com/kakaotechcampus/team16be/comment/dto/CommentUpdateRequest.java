package com.kakaotechcampus.team16be.comment.dto;


public record CommentUpdateRequest(
        Long commentId,
        String content
) {
}
