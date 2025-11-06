package com.kakaotechcampus.team16be.post.service;

import com.kakaotechcampus.team16be.common.eventListener.groupEvent.IncreaseGroupScoreByPosting;
import com.kakaotechcampus.team16be.common.eventListener.userEvent.IncreaseUserScoreByPosting;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.post.dto.CreatePostRequest;
import com.kakaotechcampus.team16be.post.dto.UpdatePostRequest;
import com.kakaotechcampus.team16be.post.exception.PostErrorCode;
import com.kakaotechcampus.team16be.post.exception.PostException;
import com.kakaotechcampus.team16be.post.repository.PostRepository;
import com.kakaotechcampus.team16be.user.domain.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final ApplicationEventPublisher eventPublisher;

    private static final ZoneId SEOUL_ZONE_ID = ZoneId.of("Asia/Seoul");

    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Group targetGroup = groupService.findGroupById(createPostRequest.groupId());
        GroupMember groupMember = groupMemberService.findByGroupAndUser(targetGroup, user);
        groupMember.checkUserIsActive();

        Post post = Post.createPost(
                user,
                targetGroup,
                createPostRequest.title(),
                createPostRequest.content(),
                createPostRequest.imageUrls()
        );

        LocalDateTime startOfToday = LocalDate.now(SEOUL_ZONE_ID).atStartOfDay();
        boolean hasAlreadyPostedToday = postRepository.existsByAuthorAndCreatedAtAfter(user, startOfToday);

        Post savedPost = postRepository.save(post);

        if (!hasAlreadyPostedToday) {
            eventPublisher.publishEvent(new IncreaseUserScoreByPosting(user));
            eventPublisher.publishEvent(new IncreaseGroupScoreByPosting(targetGroup));
        }
        return savedPost;
    }

    @Override
    @Transactional
    public void deletePost(User user, Long postId) {

        Post post = findByAuthorAndId(user, postId);
        postRepository.delete(post);
    }

    @Override
    public Post findByAuthorAndId(User user, Long postId) {
        return postRepository.findByAuthorAndId(user, postId)
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

    @Override
    public List<Post> getFeeds() {
      return postRepository.findAllByOrderByCreatedAtDesc();
    }
}
