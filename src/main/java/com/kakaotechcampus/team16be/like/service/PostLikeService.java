package com.kakaotechcampus.team16be.like.service;

import com.kakaotechcampus.team16be.like.dto.PostLikeRequest;
import com.kakaotechcampus.team16be.like.dto.PostLikeResponse;
import com.kakaotechcampus.team16be.user.domain.User;

public interface PostLikeService {
    void likePost(User user, PostLikeRequest postLikeRequest);

    void unlikePost(User user, PostLikeRequest postLikeRequest);

    PostLikeResponse getPostLikeInfo(User user, Long postId);
}
