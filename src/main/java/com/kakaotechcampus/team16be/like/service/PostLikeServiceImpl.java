package com.kakaotechcampus.team16be.like.service;

import com.kakaotechcampus.team16be.like.domain.Like;
import com.kakaotechcampus.team16be.like.dto.PostLikeResponse;
import com.kakaotechcampus.team16be.like.exception.LikeErrorCode;
import com.kakaotechcampus.team16be.like.exception.LikeException;
import com.kakaotechcampus.team16be.like.repository.PostLikeRepository;
import com.kakaotechcampus.team16be.like.dto.PostLikeRequest;
import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.post.service.PostService;
import com.kakaotechcampus.team16be.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements  PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostService postService;

    @Override
    @Transactional
    public void likePost(User user, PostLikeRequest postLikeRequest) {
        Post post = postService.findById(postLikeRequest.postId());
        post.increaseLikeCount();
        checkIsLikePost(user, post);
        Like postLike = Like.likePost(user, post);
        postLikeRepository.save(postLike);
    }

    @Override
    @Transactional
    public void unlikePost(User user, PostLikeRequest postLikeRequest) {
        Post post = postService.findById(postLikeRequest.postId());
        post.decreaseLikeCount();
        Like postLike = findByUserAndPost(user, post);
        postLikeRepository.delete(postLike);
    }

    private Like findByUserAndPost(User user, Post post) {
        return postLikeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new LikeException(LikeErrorCode.NOT_LIKED_POST));
    }

    private void checkIsLikePost(User user, Post post) {
        if (postLikeRepository.existsByUserAndPost(user, post)) {
            throw new LikeException(LikeErrorCode.ALREADY_LIKED_POST);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public PostLikeResponse getPostLikeInfo(User user, Long postId) {
        Post post = postService.findById(postId);
        boolean isLiked = isLiked(user, post);
        Long likeCount = post.getLikeCount();

        return new PostLikeResponse(likeCount, isLiked);
    }

    private boolean isLiked(User user, Post post) {
        return postLikeRepository.existsByUserAndPost(user, post);
    }
}
