package com.kakaotechcampus.team16be.comment.service;

import com.kakaotechcampus.team16be.comment.domain.Comment;
import com.kakaotechcampus.team16be.comment.dto.CommentUpdateRequest;
import com.kakaotechcampus.team16be.comment.exception.CommentErrorCode;
import com.kakaotechcampus.team16be.comment.exception.CommentException;
import com.kakaotechcampus.team16be.comment.repository.CommentRepository;
import com.kakaotechcampus.team16be.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private static void hasAuthority(User user, Comment comment) {
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CommentException(CommentErrorCode.COMMENT_NOT_AUTHORIZED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Comment findById(Long id) {
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
