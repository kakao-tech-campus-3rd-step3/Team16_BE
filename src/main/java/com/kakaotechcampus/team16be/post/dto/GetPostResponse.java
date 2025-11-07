package com.kakaotechcampus.team16be.post.dto;

import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.user.domain.User;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public record GetPostResponse(
        Long postId,
        Long groupId,
        String groupName,
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
    public static GetPostResponse from(Post post, User author, String authorProfileImageUrl, List<String> fullURLs,
                                       Integer commentCount, boolean isLike) {
        LocalDateTime createdAtKst = post.getCreatedAt()
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();

        return new GetPostResponse(
                post.getId(),
                post.getGroup().getId(),
                post.getGroup().getName(),
                author.getId(),
                author.getNickname(),
                authorProfileImageUrl,
                post.getTitle(),
                post.getContent(),
                fullURLs,
                post.getLikeCount(),
                commentCount,
                createdAtKst,
                isLike
        );
    }


}
