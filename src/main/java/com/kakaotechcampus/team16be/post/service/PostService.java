package com.kakaotechcampus.team16be.post.service;

import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.post.dto.CreatePostRequest;
import com.kakaotechcampus.team16be.post.dto.UpdatePostRequest;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface PostService {
    Post createPost(User user, CreatePostRequest createPostRequest);

    void deletePost(User user, Long postId);

    Post findByAuthorAndId(User user, Long postId);

    Post updatePost(User user, Long postId, @Valid UpdatePostRequest updatePostRequest);

    Post findById(Long postId);

    List<Post> getFeeds();
}
