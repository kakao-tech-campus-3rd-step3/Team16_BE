package com.kakaotechcampus.team16be.post.controller;

import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.post.dto.CreatePostRequest;
import com.kakaotechcampus.team16be.post.dto.GetPostResponse;
import com.kakaotechcampus.team16be.post.dto.PostIdResponse;
import com.kakaotechcampus.team16be.post.dto.UpdatePostRequest;
import com.kakaotechcampus.team16be.post.service.PostFacadeService;
import com.kakaotechcampus.team16be.post.service.PostService;
import com.kakaotechcampus.team16be.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Tag(name = "게시글 API", description = "게시글 CRUD API")
public class PostController {

    private final PostService postService;
    private final PostFacadeService postFacadeService;

    @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
    @PostMapping("/posts")
    public ResponseEntity<PostIdResponse> createPost(@LoginUser User user, @Valid @RequestBody CreatePostRequest createPostRequest) {
        Post post = postService.createPost(user, createPostRequest);
        return ResponseEntity.ok(PostIdResponse.from(post.getId()));
    }

    @Operation(summary = "게시글 조회", description = "특정 게시글을 조회합니다.")
    @GetMapping("/{groupId}/posts/{postId}")
    public ResponseEntity<GetPostResponse> getPost(@LoginUser User user,@PathVariable Long groupId, @PathVariable Long postId) {
        GetPostResponse PostResponse = postFacadeService.getPost(user,groupId, postId);
        return ResponseEntity.ok(PostResponse);
    }

    @Operation(summary = "게시글 전체 조회", description = "특정 그룹의 모든 게시글을 조회합니다.")
    @GetMapping("/{groupId}/posts")
    public ResponseEntity<List<GetPostResponse>> getAllPosts(@LoginUser User user, @PathVariable Long groupId) {
        List<GetPostResponse> PostResponses = postFacadeService.getAllPosts(user, groupId);
        return ResponseEntity.ok(PostResponses);
    }

    @Operation(summary = "게시글 삭제", description = "특정 게시글을 삭제합니다.")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@LoginUser User user, @PathVariable Long postId) {
        postService.deletePost(user, postId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시글 수정", description = "특정 게시글을 수정합니다.")
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostIdResponse> updatePost(@LoginUser User user,
                                           @PathVariable Long postId,
                                           @Valid @RequestBody UpdatePostRequest updatePostRequest
    ) {
        Post post = postService.updatePost(user, postId, updatePostRequest);
        return ResponseEntity.ok(PostIdResponse.from(post.getId()));
    }

  @Operation(summary = "피드 게시글 반환", description = "모든 게시글을 시간순으로 조회합니다.")
  @GetMapping("/posts/feeds")
  public ResponseEntity<List<GetPostResponse>> getFeeds(@LoginUser User user) {
      List<Post> posts = postService.getFeeds();
      List<GetPostResponse> PostResponses = GetPostResponse.from(posts);
      return ResponseEntity.ok(PostResponses);
  }
}
