package com.kakaotechcampus.team16be.post.service;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.repository.GroupRepository;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.repository.UserRepository;
import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.post.domain.PostLike;
import com.kakaotechcampus.team16be.post.dto.GetPostsResponse;
import com.kakaotechcampus.team16be.post.dto.PostLikeResponse;
import com.kakaotechcampus.team16be.post.dto.PostRequest;
import com.kakaotechcampus.team16be.post.dto.PostResponse;
import com.kakaotechcampus.team16be.post.repository.PostLikeRepository;
import com.kakaotechcampus.team16be.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final PostLikeRepository postLikeRepository;

    public GetPostsResponse getPosts(Long groupId, Long cursor, int limit, String search) {
        if (search == null) {
            search = "";
        }
        PageRequest pageRequest = PageRequest.of(0, limit);
        Slice<Post> posts = postRepository.findByGroupIdWithCursor(groupId, cursor, search, pageRequest);
        Long nextCursor = posts.hasNext() ? posts.getContent().get(limit - 1).getId() : null;
        return new GetPostsResponse(posts.getContent().stream().map(PostResponse::from).collect(Collectors.toList()), nextCursor);
    }

    public PostResponse createPost(Long groupId, Long memberId, PostRequest request) {
        User user = userRepository.findById(memberId).orElseThrow(() -> new RuntimeException("User not found"));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        Post post = new Post(request.title(), request.content(), request.images(), user, group);
        postRepository.save(post);
        return PostResponse.from(post);
    }

    public PostResponse updatePost(Long postId, PostRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        post.update(request.title(), request.content(), request.images());
        return PostResponse.from(post);
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public PostLikeResponse likePost(Long postId, Long memberId) {
        User user = userRepository.findById(memberId).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        postLikeRepository.findByPostAndUser(post, user).ifPresent(like -> {
            throw new RuntimeException("Already liked");
        });
        postLikeRepository.save(new PostLike(post, user));
        long likeCount = postLikeRepository.countByPost(post);
        return new PostLikeResponse(likeCount, true);
    }

    public PostLikeResponse unlikePost(Long postId, Long memberId) {
        User user = userRepository.findById(memberId).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        PostLike postLike = postLikeRepository.findByPostAndUser(post, user).orElseThrow(() -> new RuntimeException("Not liked yet"));
        postLikeRepository.delete(postLike);
        long likeCount = postLikeRepository.countByPost(post);
        return new PostLikeResponse(likeCount, false);
    }
}
