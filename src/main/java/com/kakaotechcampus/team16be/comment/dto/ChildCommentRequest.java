package com.kakaotechcampus.team16be.comment.dto;

public record ChildCommentRequest(
        Long groupId,
        Long postId,
        String content,
        Long parentId
) {
}
