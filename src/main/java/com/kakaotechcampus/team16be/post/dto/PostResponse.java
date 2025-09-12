package com.kakaotechcampus.team16be.post.dto;

import com.kakaotechcampus.team16be.post.domain.Post;

import java.util.List;

public record PostResponse(Long id, String title, String content, int viewCount, List<String> imageUrls) {
    public static PostResponse from(Post post) {
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getViewCount(), post.getImageUrls());
    }
}
