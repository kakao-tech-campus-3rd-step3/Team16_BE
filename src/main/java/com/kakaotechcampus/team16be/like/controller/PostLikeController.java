package com.kakaotechcampus.team16be.like.controller;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.like.dto.PostLikeRequest;
import com.kakaotechcampus.team16be.like.dto.PostLikeResponse;
import com.kakaotechcampus.team16be.like.service.PostLikeService;
import com.kakaotechcampus.team16be.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "게시글 좋아요", description = "게시글 좋아요 API")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @Operation(summary = "게시글 좋아요", description = "사용자가 특정 게시글에 좋아요를 누릅니다.")
    @PostMapping("/likes")
    public ResponseEntity<Void> likePost(@LoginUser User user, @RequestBody PostLikeRequest postLikeRequest) {
        postLikeService.likePost(user, postLikeRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "게시글 좋아요 취소", description = "사용자가 특정 게시글에 눌렀던 좋아요를 취소합니다.")
    @DeleteMapping("/likes")
    public ResponseEntity<Void> unlikePost(@LoginUser User user, @RequestBody PostLikeRequest postLikeRequest) {
        postLikeService.unlikePost(user, postLikeRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "게시글 좋아요 수 및 현재 사용자의 좋아요 유무 조회", description = "특정 게시글의 좋아요 수 및 현재 사용자의 좋아요 유무를 조회합니다.")
    @GetMapping("/{postId}/likes")
    public ResponseEntity<PostLikeResponse> getPostLikeCount(@LoginUser User user, @PathVariable Long postId) {
        PostLikeResponse postLikeResponse = postLikeService.getPostLikeInfo(user, postId);
        return ResponseEntity.ok(postLikeResponse);
    }
}
