package com.kakaotechcampus.team16be.comment.dto;

public record CommentIdResponse(
        Long commentId
) {
    public static CommentIdResponse from(Long commentId) {
        return new CommentIdResponse(commentId);
    }
}
