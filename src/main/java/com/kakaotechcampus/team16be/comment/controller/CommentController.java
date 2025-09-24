package com.kakaotechcampus.team16be.comment.controller;

import com.kakaotechcampus.team16be.comment.domain.Comment;
import com.kakaotechcampus.team16be.comment.dto.CommentIdResponse;
import com.kakaotechcampus.team16be.comment.dto.CommentRequest;
import com.kakaotechcampus.team16be.comment.dto.CommentResponse;
import com.kakaotechcampus.team16be.comment.dto.CommentUpdateRequest;
import com.kakaotechcampus.team16be.comment.service.CommentService;
import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "댓글 API", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "부모 댓글 작성", description = "부모 댓글을 작성합니다.(대댓글 X)")
    @PostMapping("/comments")
    public ResponseEntity<CommentIdResponse> createParentComment(
            @LoginUser User user,
            @RequestBody CommentRequest commentRequest
    ) {
        Long commentId= commentService.createParentComment(user, commentRequest);
        return ResponseEntity.ok(CommentIdResponse.from(commentId));
    }

    @Operation(summary = "대댓글 작성", description = "부모댓글에 대댓글을 작성합니다.")
    @PostMapping("/comments/reply")
    public ResponseEntity<CommentIdResponse> createChildComment(
            @LoginUser User user,
            @RequestBody CommentRequest commentRequest
    ) {
        Long commentId = commentService.createChildComment(user, commentRequest);
        return ResponseEntity.ok(CommentIdResponse.from(commentId));
    }

    @Operation(summary = "특정 게시글의 댓글 전부 조회", description = "특정 게시글의 모든 댓글을 조회합니다.")
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getAllComments(
            @PathVariable Long postId
    ) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(CommentResponse.from(comments));
    }

    @Operation(summary = "댓글 수정", description = "특정 댓글을 수정합니다.")
    @PutMapping("/comments")
    public ResponseEntity<CommentIdResponse> updateComment(
            @LoginUser User user,
            @RequestBody CommentUpdateRequest commentUpdateRequest
    ) {
        Long commentId = commentService.updateComment(user, commentUpdateRequest);
        return ResponseEntity.ok(CommentIdResponse.from(commentId));
    }

    @Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다.")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @LoginUser User user,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(user, commentId);
        return ResponseEntity.noContent().build();
    }

}
    


