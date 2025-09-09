package com.kakaotechcampus.team16be.comment.dto;

import com.kakaotechcampus.team16be.comment.domain.Comment;

public record CommentResponse(Long id, String content) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getContent());
    }
}
