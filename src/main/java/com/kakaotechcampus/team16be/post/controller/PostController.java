package com.kakaotechcampus.team16be.post.controller;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.post.dto.GetPostsResponse;
import com.kakaotechcampus.team16be.post.dto.PostLikeResponse;
import com.kakaotechcampus.team16be.post.dto.PostRequest;
import com.kakaotechcampus.team16be.post.dto.PostResponse;
import com.kakaotechcampus.team16be.post.service.PostService;
import com.kakaotechcampus.team16be.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @GetMapping("/groups/{groupId}/posts")
    public ResponseEntity<GetPostsResponse> getPosts(@PathVariable Long groupId,
                                                     @RequestParam(required = false) Long cursor,
                                                     @RequestParam(defaultValue = "10") int limit,
                                                     @RequestParam(required = false) String search) {
        return ResponseEntity.ok(postService.getPosts(groupId, cursor, limit, search));
    }

    @PostMapping("/groups/{groupId}/posts")
    public ResponseEntity<PostResponse> createPost(@PathVariable Long groupId, 
                                                       @RequestBody PostRequest request, 
                                                       @LoginUser User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(groupId, user.getId(), request));
    }

    @PatchMapping("/groups/{groupId}/posts/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long groupId, 
                                                       @PathVariable Long postId, 
                                                       @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.updatePost(postId, request));
    }

    @DeleteMapping("/groups/{groupId}/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long groupId, 
                                                   @PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/groups/{groupId}/posts/{postId}/likes")
    public ResponseEntity<PostLikeResponse> likePost(@PathVariable Long groupId, 
                                                     @PathVariable Long postId, 
                                                     @LoginUser User user) {
        return ResponseEntity.ok(postService.likePost(postId, user.getId()));
    }

    @DeleteMapping("/groups/{groupId}/posts/{postId}/likes")
    public ResponseEntity<PostLikeResponse> unlikePost(@PathVariable Long groupId, 
                                                       @PathVariable Long postId, 
                                                       @LoginUser User user) {
        return ResponseEntity.ok(postService.unlikePost(postId, user.getId()));
    }
}
