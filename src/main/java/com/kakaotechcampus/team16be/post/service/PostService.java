package com.kakaotechcampus.team16be.post.service;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.post.dto.CreatePostRequest;
import com.kakaotechcampus.team16be.post.dto.GetPostResponse;
import com.kakaotechcampus.team16be.post.dto.UpdatePostRequest;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    Post createPost(User user, CreatePostRequest createPostRequest);

    GetPostResponse getPost(User user, Long groupId, Long postId);

    List<GetPostResponse> getAllPosts(User user, Long groupId);

    void deletePost(User user, Long postId);

    Post findByAuthorAndId(User user, Long postId);

    Post updatePost(User user, Long postId, @Valid UpdatePostRequest updatePostRequest);

    Post findById(Long postId);
}
