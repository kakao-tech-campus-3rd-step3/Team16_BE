package com.kakaotechcampus.team16be.post.controller;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.post.dto.GetPostsResponse;
import com.kakaotechcampus.team16be.post.dto.PostLikeResponse;
import com.kakaotechcampus.team16be.post.dto.PostRequest;
import com.kakaotechcampus.team16be.post.dto.PostResponse;
import com.kakaotechcampus.team16be.post.service.PostService;
import com.kakaotechcampus.team16be.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "게시글 API", description = "게시글 관련 API")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 목록 조회", description = "그룹 내 게시글 목록을 조회합니다.")
    @GetMapping("/groups/{groupId}/posts")
    public ResponseEntity<GetPostsResponse> getPosts(@PathVariable Long groupId,
                                                     @RequestParam(required = false) Long cursor,
                                                     @RequestParam(defaultValue = "10") int limit,
                                                     @RequestParam(required = false) String search) {
        return ResponseEntity.ok(postService.getPosts(groupId, cursor, limit, search));
    }

    @Operation(summary = "게시글 생성", description = "그룹 내 새로운 게시글을 작성합니다.")
    @PostMapping("/groups/{groupId}/posts")
    public ResponseEntity<PostResponse> createPost(@PathVariable Long groupId, 
                                                       @RequestBody PostRequest request, 
                                                       @LoginUser User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(groupId, user.getId(), request));
    }

    @Operation(summary = "게시글 수정", description = "기존 게시글을 수정합니다.")
    @PatchMapping("/groups/{groupId}/posts/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long groupId, 
                                                       @PathVariable Long postId, 
                                                       @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.updatePost(postId, request));
    }

    @Operation(summary = "게시글 삭제", description = "그룹 내 특정 게시글을 삭제합니다.")
    @DeleteMapping("/groups/{groupId}/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long groupId, 
                                                   @PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시글 좋아요", description = "게시글에 좋아요를 추가합니다.")
    @PostMapping("/groups/{groupId}/posts/{postId}/likes")
    public ResponseEntity<PostLikeResponse> likePost(@PathVariable Long groupId, 
                                                     @PathVariable Long postId, 
                                                     @LoginUser User user) {
        return ResponseEntity.ok(postService.likePost(postId, user.getId()));
    }

    @Operation(summary = "게시글 좋아요 취소", description = "게시글에 대한 좋아요를 취소합니다.")
    @DeleteMapping("/groups/{groupId}/posts/{postId}/likes")
    public ResponseEntity<PostLikeResponse> unlikePost(@PathVariable Long groupId, 
                                                       @PathVariable Long postId, 
                                                       @LoginUser User user) {
        return ResponseEntity.ok(postService.unlikePost(postId, user.getId()));
    }
}
