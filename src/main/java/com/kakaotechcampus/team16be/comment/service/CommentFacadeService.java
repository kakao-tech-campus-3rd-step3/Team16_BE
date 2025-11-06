package com.kakaotechcampus.team16be.comment.service;

import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import com.kakaotechcampus.team16be.comment.domain.Comment;
import com.kakaotechcampus.team16be.comment.dto.ChildCommentRequest;
import com.kakaotechcampus.team16be.comment.dto.CommentResponse;
import com.kakaotechcampus.team16be.comment.dto.ParentCommentRequest;
import com.kakaotechcampus.team16be.comment.repository.CommentRepository;
import com.kakaotechcampus.team16be.common.eventListener.userEvent.IncreaseScoreByComment;
import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.post.service.PostService;
import com.kakaotechcampus.team16be.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentFacadeService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final CommentService commentService;
    private final ApplicationEventPublisher eventPublisher;
    private final S3UploadPresignedUrlService s3UploadPresignedUrlService;


    @Transactional
    public Long createParentComment(User user, ParentCommentRequest parentCommentRequest) {
        Post post = postService.findById(parentCommentRequest.postId());

        Comment parentComment = Comment.createComment(parentCommentRequest.content(), post, user, null);

        Comment savedComment = commentRepository.save(parentComment);

        eventPublisher.publishEvent(new IncreaseScoreByComment(user));

        return savedComment.getId();
    }

    @Transactional
    public Long createChildComment(User user, ChildCommentRequest childCommentRequest) {
        Post post = postService.findById(childCommentRequest.postId());

        Comment comment = commentService.findById(childCommentRequest.parentId());

        Comment childComment = Comment.createComment(childCommentRequest.content(), post, user, comment);
        Comment saveComment = commentRepository.save(childComment);

        eventPublisher.publishEvent(new IncreaseScoreByComment(user));

        return saveComment.getId();
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        Post post = postService.findById(postId);

        List<Comment> allComments = commentRepository.findAllByPostWithUserAndParent(post);

        return allComments.stream()
                .map(comment -> {
                    String profileImageKey = comment.getUser().getProfileImageUrl();
                    String publicUrl = (profileImageKey != null && !profileImageKey.isEmpty())
                            ? s3UploadPresignedUrlService.getPublicUrl(profileImageKey)
                            : s3UploadPresignedUrlService.getPublicUrl("");
                    Long parentId = (comment.getParentComment() != null) ? comment.getParentComment().getId() : null;

                    return new CommentResponse(
                            comment.getId(),
                            comment.getUser().getId(),
                            comment.getUser().getNickname(),
                            publicUrl,
                            comment.getContent(),
                            comment.getCreatedAt().toString(),
                            comment.getUpdatedAt().toString(),
                            parentId
                    );
                })
                .toList();
    }

}
