package com.kakaotechcampus.team16be.comment.service;

import com.kakaotechcampus.team16be.comment.domain.Comment;
import com.kakaotechcampus.team16be.comment.dto.CommentRequest;
import com.kakaotechcampus.team16be.comment.dto.CommentUpdateRequest;
import com.kakaotechcampus.team16be.comment.exception.CommentErrorCode;
import com.kakaotechcampus.team16be.comment.exception.CommentException;
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
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;


    @Transactional
    @Override
    public Long createParentComment(User user, CommentRequest commentRequest) {
        Post post = postService.findById(commentRequest.postId());

        Comment parentComment = Comment.createComment(commentRequest.content(), post, user, null);
        commentRepository.save(parentComment);
        return parentComment.getId();
    }

    @Override
    @Transactional
    public Long createChildComment(User user,CommentRequest commentRequest) {
        Post post = postService.findById(commentRequest.postId());

        Comment comment = findById(commentRequest.parentId());

        Comment childComment = Comment.createComment(commentRequest.content(), post, user, comment);

        return commentRepository.save(childComment).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByPostId(Long postId) {
        Post post = postService.findById(postId);
        return commentRepository.findAllByPost(post);
    }

    private Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));
    }

    @Override
    @Transactional
    public Long updateComment(User user, CommentUpdateRequest commentUpdateRequest) {
        Comment comment = findById(commentUpdateRequest.commentId());
        hasAuthority(user, comment);

        comment.updateContent(commentUpdateRequest.content());

        return comment.getId();
    }

    private static void hasAuthority(User user, Comment comment) {
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CommentException(CommentErrorCode.COMMENT_NOT_AUTHORIZED);
        }
    }

    @Override
    @Transactional
    public void deleteComment(User user, Long commentId) {
        Comment comment = findById(commentId);
        hasAuthority(user, comment);

        if (comment.getParentComment() == null) {
            for (Comment child : comment.getChildComments()) {
                child.updateParentCommentNull();
            }
        }
        commentRepository.delete(comment);
    }

}
