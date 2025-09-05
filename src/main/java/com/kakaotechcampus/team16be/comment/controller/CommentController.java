package com.kakaotechcampus.team16be.comment.controller;

import com.kakaotechcampus.team16be.comment.dto.CommentRequest;
import com.kakaotechcampus.team16be.comment.dto.CommentResponse;
import com.kakaotechcampus.team16be.comment.dto.GetCommentsResponse;
import com.kakaotechcampus.team16be.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/groups/{groupId}/posts/{postId}/comments")
    public ResponseEntity<GetCommentsResponse> getComments(@PathVariable Long postId, @RequestParam(required = false) Long parentId, @RequestParam(required = false) Long cursor, @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(commentService.getComments(postId, parentId, cursor, limit));
    }

    @PostMapping("/groups/{groupId}/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long postId, @RequestBody CommentRequest request) {
        Long memberId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(postId, memberId, request));
    }

    @PatchMapping("/groups/{groupId}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.updateComment(commentId, request));
    }

    @DeleteMapping("/groups/{groupId}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
