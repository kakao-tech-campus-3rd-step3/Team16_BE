package com.kakaotechcampus.team16be.comment.controller;

import com.kakaotechcampus.team16be.comment.dto.ChildCommentRequest;
import com.kakaotechcampus.team16be.comment.dto.CommentIdResponse;
import com.kakaotechcampus.team16be.comment.dto.CommentResponse;
import com.kakaotechcampus.team16be.comment.dto.CommentUpdateRequest;
import com.kakaotechcampus.team16be.comment.dto.ParentCommentRequest;
import com.kakaotechcampus.team16be.comment.service.CommentFacadeService;
import com.kakaotechcampus.team16be.comment.service.CommentService;
import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "댓글 API", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;
    private final CommentFacadeService commentFacadeService;

    @Operation(summary = "부모 댓글 작성", description = "부모 댓글을 작성합니다.(대댓글 X)")
    @PostMapping("/comments")
    public ResponseEntity<CommentIdResponse> createParentComment(
            @LoginUser User user,
            @RequestBody ParentCommentRequest parentCommentRequest
    ) {
        Long commentId = commentFacadeService.createParentComment(user, parentCommentRequest);
        return ResponseEntity.ok(CommentIdResponse.from(commentId));
    }

    @Operation(summary = "대댓글 작성", description = "부모댓글에 대댓글을 작성합니다.")
    @PostMapping("/comments/reply")
    public ResponseEntity<CommentIdResponse> createChildComment(
            @LoginUser User user,
            @RequestBody ChildCommentRequest childCommentRequest
    ) {
        Long commentId = commentFacadeService.createChildComment(user, childCommentRequest);
        return ResponseEntity.ok(CommentIdResponse.from(commentId));
    }

    @Operation(summary = "특정 게시글의 댓글 전부 조회", description = "특정 게시글의 모든 댓글을 조회합니다.")
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getAllComments(
            @PathVariable Long postId
    ) {
        List<CommentResponse> comments = commentFacadeService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
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
