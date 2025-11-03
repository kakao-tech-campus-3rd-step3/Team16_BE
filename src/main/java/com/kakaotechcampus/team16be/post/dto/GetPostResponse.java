package com.kakaotechcampus.team16be.post.dto;

import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public record GetPostResponse(
        Long postId,
        Long authorId,
        String authorNickname,
        String authorProfileImageUrl,
        String title,
        String content,
        List<String> imageUrls,
        Long likeCount,
        Integer commentCount,
        LocalDateTime createdAt,
        boolean isLike
) {
    public static GetPostResponse from(Post post, User author, List<String> fullURLs, Integer commentCount, boolean isLike) {
        return new GetPostResponse(
                post.getId(),
                author.getId(),
                author.getNickname(),
                author.getProfileImageUrl(),
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
