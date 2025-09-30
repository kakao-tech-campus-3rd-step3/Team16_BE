package com.kakaotechcampus.team16be.comment.service;

import com.kakaotechcampus.team16be.comment.domain.Comment;
import com.kakaotechcampus.team16be.comment.dto.CommentUpdateRequest;
import com.kakaotechcampus.team16be.user.domain.User;

public interface CommentService {

    Long updateComment(User user, CommentUpdateRequest commentUpdateRequest);

    void deleteComment(User user, Long commentId);

    Comment findById(Long id);
}
