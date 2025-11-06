package com.kakaotechcampus.team16be.post.service;

import com.kakaotechcampus.team16be.aws.service.S3UploadPresignedUrlService;
import com.kakaotechcampus.team16be.comment.service.CommentFacadeService;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.like.dto.PostLikeResponse;
import com.kakaotechcampus.team16be.like.service.PostLikeService;
import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.post.dto.GetPostResponse;
import com.kakaotechcampus.team16be.post.exception.PostErrorCode;
import com.kakaotechcampus.team16be.post.exception.PostException;
import com.kakaotechcampus.team16be.post.repository.PostRepository;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostFacadeService {

    private final GroupService groupService;
    private final PostRepository postRepository;
    private final CommentFacadeService commentFacadeService;
    private final PostLikeService postLikeService;
    private final S3UploadPresignedUrlService s3UploadPresignedUrlService;
    private final UserService userService;


    @Transactional(readOnly = true)
    public GetPostResponse getPost(User user, Long groupId, Long postId) {
        Group targetGroup = groupService.findGroupById(groupId);
        Post post = postRepository.findByIdAndGroup(postId, targetGroup)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        Integer commentCount = commentFacadeService.getCommentsByPostId(postId).size();
        PostLikeResponse postLikeResponse = postLikeService.getPostLikeInfo(user, postId);

        List<String> fullURLs = post.getImageUrls().stream()
                .map(s3UploadPresignedUrlService::getUserPublicUrl)
                .toList();
        User author = post.getAuthor();
        String authorProfileImageUrl = author.getProfileImageUrl();
        String authorProfilePublicUrl = (authorProfileImageUrl != null && !authorProfileImageUrl.isEmpty())
                ? s3UploadPresignedUrlService.getUserPublicUrl(authorProfileImageUrl)
                : s3UploadPresignedUrlService.getUserPublicUrl("");

        return GetPostResponse.from(post, author, authorProfilePublicUrl, fullURLs, commentCount,
                postLikeResponse.isLiked());
    }

    @Transactional(readOnly = true)
    public List<GetPostResponse> getAllPosts(User user, Long groupId) {
        Group targetGroup = groupService.findGroupById(groupId);
        List<Post> posts = postRepository.findByGroupOrderByCreatedAtDesc(targetGroup);

        return posts.stream()
                .map(post -> {
                    List<String> fullURLs = post.getImageUrls().stream()
                            .map(s3UploadPresignedUrlService::getUserPublicUrl)
                            .toList();
                    Integer commentCount = commentFacadeService.getCommentsByPostId(post.getId()).size();
                    PostLikeResponse postLikeResponse = postLikeService.getPostLikeInfo(user, post.getId());
                    User author = post.getAuthor();
                    String authorProfileKey = author.getProfileImageUrl();
                    String authorProfilePublicUrl = (authorProfileKey != null && !authorProfileKey.isEmpty())
                            ? s3UploadPresignedUrlService.getUserPublicUrl(authorProfileKey)
                            : s3UploadPresignedUrlService.getUserPublicUrl("");

                    return GetPostResponse.from(
                            post,
                            author,
                            authorProfilePublicUrl,
                            fullURLs,
                            commentCount,
                            postLikeResponse.isLiked()
                    );
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GetPostResponse> getFeeds(User user) {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();

        return posts.stream()
                .map(post -> {
                    List<String> fullURLs = post.getImageUrls().stream()
                            .map(s3UploadPresignedUrlService::getUserPublicUrl)
                            .toList();
                    Integer commentCount = commentFacadeService.getCommentsByPostId(post.getId()).size();
                    PostLikeResponse postLikeResponse = postLikeService.getPostLikeInfo(user, post.getId());
                    User author = post.getAuthor();
                    String profileImageUrl = author.getProfileImageUrl();
                    String authorProfilePublicUrl = (profileImageUrl != null && !profileImageUrl.isEmpty())
                            ? s3UploadPresignedUrlService.getUserPublicUrl(profileImageUrl)
                            : s3UploadPresignedUrlService.getUserPublicUrl("");

                    return GetPostResponse.from(
                            post,
                            author,
                            authorProfilePublicUrl,
                            fullURLs,
                            commentCount,
                            postLikeResponse.isLiked()
                    );
                })
                .toList();
    }

    public void adminDeletePost(User user, Long groupId, Long postId) {
        Group targetGroup = groupService.findGroupById(groupId);
        groupService.checkGroupLeader(user, targetGroup);

        Post post = postRepository.findByIdAndGroup(postId, targetGroup)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        postRepository.delete(post);
    }
}
