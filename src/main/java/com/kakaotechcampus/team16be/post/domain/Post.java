package com.kakaotechcampus.team16be.post.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.group.domain.Group;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "posts")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 1000)
    private String content;

    @ElementCollection
    @CollectionTable(
            name = "post_image_urls",
            joinColumns = @JoinColumn(name = "post_id")
    )
    @Column(name = "image_url", length = 1000)
    private List<String> imageUrls = new ArrayList<>();

    private Long likeCount = 0L;

    @Builder
    public Post(String author, Group group, String title, String content, List<String> imageUrls) {
        this.author = author;
        this.group = group;
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
    }

    public static Post createPost(String userNickname, Group group, String title, String content, List<String> imageUrls) {
        return com.kakaotechcampus.team16be.post.domain.Post.builder()
                .author(userNickname)
                .group(group)
                .title(title)
                .content(content)
                .imageUrls(imageUrls)
                .build();
    }

    public Post updatePost(String title, String content, List<String> imageUrls) {
        if (title != null || !(title.isBlank())) {
            this.title = title;
        }
        if (content != null || (!content.isBlank())) {
            this.content = content;
        }
        if (imageUrls != null || !(imageUrls.isEmpty())) {
            this.imageUrls = imageUrls;
        }
        return this;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
