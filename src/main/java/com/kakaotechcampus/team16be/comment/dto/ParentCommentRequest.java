package com.kakaotechcampus.team16be.comment.dto;

public record ParentCommentRequest(
        Long postId,
        String content
) {
}
