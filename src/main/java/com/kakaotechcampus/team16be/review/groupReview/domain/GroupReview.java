package com.kakaotechcampus.team16be.review.groupReview.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class GroupReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id",nullable = false)
    private Group group;

    @NotBlank
    private String content;

    protected GroupReview() {
    }

    @Builder
    public GroupReview(User user, Group group, String content) {
        this.user = user;
        this.group = group;
        this.content = content;
    }

    public static GroupReview createReview(User user, Group targetGroup, String content) {
        return GroupReview.builder().
                user(user).
                group(targetGroup).
                content(content).
                build();
    }
}
