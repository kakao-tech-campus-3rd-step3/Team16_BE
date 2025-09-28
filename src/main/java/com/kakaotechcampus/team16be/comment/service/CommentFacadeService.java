package com.kakaotechcampus.team16be.comment.service;

import com.kakaotechcampus.team16be.comment.domain.Comment;
import com.kakaotechcampus.team16be.comment.dto.ChildCommentRequest;
import com.kakaotechcampus.team16be.comment.dto.ParentCommentRequest;
import com.kakaotechcampus.team16be.comment.repository.CommentRepository;
import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.post.service.PostService;
import com.kakaotechcampus.team16be.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentFacadeService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final CommentService commentService;


    @Transactional
    public Long createParentComment(User user, ParentCommentRequest parentCommentRequest) {
        Post post = postService.findById(parentCommentRequest.postId());

        Comment parentComment = Comment.createComment(parentCommentRequest.content(), post, user, null);
        commentRepository.save(parentComment);
        return parentComment.getId();
    }

    @Transactional
    public Long createChildComment(User user, ChildCommentRequest childCommentRequest) {
        Post post = postService.findById(childCommentRequest.postId());

        Comment comment = commentService.findById(childCommentRequest.parentId());

        Comment childComment = Comment.createComment(childCommentRequest.content(), post, user, comment);

        return commentRepository.save(childComment).getId();
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsByPostId(Long postId) {
        Post post = postService.findById(postId);
        return commentRepository.findAllByPost(post);
    }

}
