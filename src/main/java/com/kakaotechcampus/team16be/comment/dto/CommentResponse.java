package com.kakaotechcampus.team16be.comment.dto;

import com.kakaotechcampus.team16be.comment.domain.Comment;

import java.util.List;

public record CommentResponse(
        Long commentId,
        String userNickname,
        String content,
        String createdAt,
        String updatedAt,
        Long parentCommentId
) {
    public static List<CommentResponse> from(List<Comment> comments) {
        return comments.stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getUser().getNickname(),
                        comment.getContent(),
                        comment.getCreatedAt().toString(),
                        comment.getUpdatedAt().toString(),
                        comment.getParentComment() != null ? comment.getParentComment().getId() : null
                ))
                .toList();
    }
}
