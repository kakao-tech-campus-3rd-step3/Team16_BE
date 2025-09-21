package com.kakaotechcampus.team16be.post.dto;

import com.kakaotechcampus.team16be.post.domain.Post;

import java.util.List;

public record GetPostResponse(
        Long postId,
        String authorNickname,
        String title,
        String content,
        List<String> imageUrls
//        String likeCount,
//        String commentCount,
//        boolean isLikedByCurrentUser
) {
    public static GetPostResponse from(Post post, List<String> fullURLs) {
        return new GetPostResponse(
                post.getId(),
                post.getAuthor(),
                post.getTitle(),
                post.getContent(),
                fullURLs
        );
    }
}
