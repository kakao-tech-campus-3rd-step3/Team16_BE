package com.kakaotechcampus.team16be.comment.service;

import com.kakaotechcampus.team16be.comment.domain.Comment;
import com.kakaotechcampus.team16be.comment.dto.CommentRequest;
import com.kakaotechcampus.team16be.comment.dto.CommentResponse;
import com.kakaotechcampus.team16be.comment.dto.GetCommentsResponse;
import com.kakaotechcampus.team16be.comment.repository.CommentRepository;
import com.kakaotechcampus.team16be.member.domain.Member;
import com.kakaotechcampus.team16be.member.repository.MemberRepository;
import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public GetCommentsResponse getComments(Long postId, Long parentId, Long cursor, int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        Slice<Comment> comments = commentRepository.findByPostIdAndParentIdWithCursor(postId, parentId, cursor, pageRequest);
        Long nextCursor = comments.hasNext() ? comments.getContent().get(limit - 1).getId() : null;
        return new GetCommentsResponse(comments.getContent().stream().map(CommentResponse::from).collect(Collectors.toList()), nextCursor);
    }

    public CommentResponse createComment(Long postId, Long memberId, CommentRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));
        Comment parent = null;
        if (request.parentId() != null) {
            parent = commentRepository.findById(request.parentId()).orElseThrow(() -> new RuntimeException("Parent comment not found"));
        }
        Comment comment = new Comment(request.content(), post, member, parent);
        commentRepository.save(comment);
        return CommentResponse.from(comment);
    }

    public CommentResponse updateComment(Long commentId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.update(request.content());
        return CommentResponse.from(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
