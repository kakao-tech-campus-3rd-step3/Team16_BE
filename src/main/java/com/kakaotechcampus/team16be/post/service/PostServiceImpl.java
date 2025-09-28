package com.kakaotechcampus.team16be.post.service;

import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import com.kakaotechcampus.team16be.comment.service.CommentService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    private final S3UploadPresignedUrlService s3UploadPresignedUrlService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;

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

        return postRepository.save(post);
    }

    @Override
    @Transactional(readOnly = true)
    public GetPostResponse getPost(User user, Long groupId, Long postId) {
        Group targetGroup = groupService.findGroupById(groupId);
        Post post = postRepository.findByIdAndGroup(postId, targetGroup)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        Integer commentCount = commentService.getCommentsByPostId(postId).size();
        PostLikeResponse postLikeResponse = postLikeService.getPostLikeInfo(user, postId);

        List<String> fullURLs = post.getImageUrls().stream()
                .map(s3UploadPresignedUrlService::getPublicUrl)
                .toList();

        return GetPostResponse.from(post, fullURLs, commentCount, postLikeResponse.isLiked());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetPostResponse> getAllPosts(User user, Long groupId) {
        Group targetGroup = groupService.findGroupById(groupId);
        List<Post> posts = postRepository.findByGroup(targetGroup);

        return posts.stream()
                .map(post -> {
                    List<String> fullURLs = post.getImageUrls().stream()
                            .map(s3UploadPresignedUrlService::getPublicUrl)
                            .toList();
                    Integer commentCount = commentService.getCommentsByPostId(post.getId()).size();
                    PostLikeResponse postLikeResponse = postLikeService.getPostLikeInfo(user, post.getId());
                    return GetPostResponse.from(post, fullURLs, commentCount,postLikeResponse.isLiked());
                })
                .toList();
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
