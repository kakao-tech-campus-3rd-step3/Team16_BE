package com.kakaotechcampus.team16be.comment.dto;

public record CommentRequest(
        Long groupId,
        Long postId,
        String content,
        Long parentId
) {
}
