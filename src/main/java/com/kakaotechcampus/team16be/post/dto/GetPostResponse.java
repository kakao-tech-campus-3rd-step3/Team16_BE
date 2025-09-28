package com.kakaotechcampus.team16be.post.dto;

import com.kakaotechcampus.team16be.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

public record GetPostResponse(
        Long postId,
        String authorNickname,
        String title,
        String content,
        List<String> imageUrls,
        Long likeCount,
        Integer commentCount,
        LocalDateTime createdAt,
        boolean isLike
) {
    public static GetPostResponse from(Post post, List<String> fullURLs, Integer commentCount, boolean isLike) {
        return new GetPostResponse(
                post.getId(),
                post.getAuthor(),
                post.getTitle(),
                post.getContent(),
                fullURLs,
                post.getLikeCount(),
                commentCount,
                post.getCreatedAt(),
                isLike
        );
    }
}
