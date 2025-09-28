package com.kakaotechcampus.team16be.comment.service;

import com.kakaotechcampus.team16be.comment.domain.Comment;
import com.kakaotechcampus.team16be.comment.dto.CommentRequest;
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
    public Long createParentComment(User user, CommentRequest commentRequest) {
        Post post = postService.findById(commentRequest.postId());

        Comment parentComment = Comment.createComment(commentRequest.content(), post, user, null);
        commentRepository.save(parentComment);
        return parentComment.getId();
    }

    @Transactional
    public Long createChildComment(User user,CommentRequest commentRequest) {
        Post post = postService.findById(commentRequest.postId());

        Comment comment = commentService.findById(commentRequest.parentId());

        Comment childComment = Comment.createComment(commentRequest.content(), post, user, comment);

        return commentRepository.save(childComment).getId();
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsByPostId(Long postId) {
        Post post = postService.findById(postId);
        return commentRepository.findAllByPost(post);
    }

}
