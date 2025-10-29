package com.kakaotechcampus.team16be.post.service;

import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import com.kakaotechcampus.team16be.comment.service.CommentFacadeService;
import com.kakaotechcampus.team16be.comment.service.CommentService;
import com.kakaotechcampus.team16be.common.eventListener.IncreaseUserScore;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.like.dto.PostLikeResponse;
import com.kakaotechcampus.team16be.like.service.PostLikeService;
import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.post.dto.CreatePostRequest;
import com.kakaotechcampus.team16be.post.dto.GetPostResponse;
import com.kakaotechcampus.team16be.post.dto.UpdatePostRequest;
import com.kakaotechcampus.team16be.post.exception.PostErrorCode;
import com.kakaotechcampus.team16be.post.exception.PostException;
import com.kakaotechcampus.team16be.post.repository.PostRepository;
import com.kakaotechcampus.team16be.user.domain.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Group targetGroup = groupService.findGroupById(createPostRequest.groupId());
        GroupMember groupMember = groupMemberService.findByGroupAndUser(targetGroup, user);
        groupMember.checkUserIsActive();

        Post post = Post.createPost(
                user.getNickname(),
                targetGroup,
                createPostRequest.title(),
                createPostRequest.content(),
                createPostRequest.imageUrls()
        );
        LocalDateTime startOfToday = LocalDate.now(ZoneId.of("Asia/Seoul")).atStartOfDay();
        boolean hasAlreadyPostedToday = postRepository.existsByAuthorAndCreatedAtAfter(user.getNickname(), startOfToday);

        if (!hasAlreadyPostedToday) {
            eventPublisher.publishEvent(new IncreaseUserScore(user));
        }
        return postRepository.save(post);

    }

    @Override
    @Transactional
    public void deletePost(User user, Long postId) {
        Post post = findByAuthorAndId(user, postId);
        postRepository.delete(post);
    }

    @Override
    public Post findByAuthorAndId(User user, Long postId) {
        return postRepository.findByAuthorAndId(user.getNickname(), postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
    }

    @Override
    @Transactional
    public Post updatePost(User user, Long postId, UpdatePostRequest updatePostRequest) {
        Post post = findByAuthorAndId(user, postId);
        return post.updatePost(
                updatePostRequest.title(),
                updatePostRequest.content(),
                updatePostRequest.imageUrls()
        );
    }

    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
    }
}
