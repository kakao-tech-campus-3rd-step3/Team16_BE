package com.kakaotechcampus.team16be.comment.controller;

import com.kakaotechcampus.team16be.comment.dto.CommentRequest;
import com.kakaotechcampus.team16be.comment.dto.CommentResponse;
import com.kakaotechcampus.team16be.comment.dto.GetCommentsResponse;
import com.kakaotechcampus.team16be.comment.service.CommentService;
import com.kakaotechcampus.team16be.common.annotation.LoginUser;
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
@Tag(name = "댓글 API", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 조회", description = "특정 게시글의 댓글 목록을 조회합니다. 부모 댓글 ID를 지정하면 대댓글 조회도 가능합니다.")
    @GetMapping("/groups/{groupId}/posts/{postId}/comments")
    public ResponseEntity<GetCommentsResponse> getComments(@PathVariable Long postId,
                                                         @RequestParam(required = false) Long parentId,
                                                         @RequestParam(required = false) Long cursor,
                                                         @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(commentService.getComments(postId, parentId, cursor, limit));
    }

    @Operation(summary = "댓글 작성", description = "특정 게시글에 댓글을 작성합니다.")
    @PostMapping("/groups/{groupId}/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long postId,
                                                       @RequestBody CommentRequest request,
                                                       @LoginUser User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(postId, user.getId(), request));
    }

    @Operation(summary = "댓글 수정", description = "작성한 댓글을 수정합니다.")
    @PatchMapping("/groups/{groupId}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId, 
                                                       @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.updateComment(commentId, request));
    }

    @Operation(summary = "댓글 삭제", description = "작성한 댓글을 삭제합니다.")
    @DeleteMapping("/groups/{groupId}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
