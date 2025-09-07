package com.kakaotechcampus.team16be.group.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.group.exception.ErrorCode;
import com.kakaotechcampus.team16be.group.exception.GroupException;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "groups") // Group 예약어로 인한 변경
public class Group extends BaseEntity {

    private static final String DEFAULT_COVER_IMAGE_URL = "https://your-s3-bucket/defaults/group_default.png";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String intro;

    private String coverImageUrl;

    private String category;

    @Min(1)
    private Integer capacity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private final SafetyTag safetyTag = SafetyTag.SAFE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaderUserId", nullable = false)
    private User leader;

    @Builder
    protected Group(User user, String name, String intro, Integer capacity) {
        this.leader = user;
        this.name = name;
        this.intro = intro;
        this.capacity = capacity;
    }

    public Group() {

    }

    public static Group createGroup(User user, String name, String intro, Integer capacity) {
        return new Group(user, name, intro, capacity);
    }


    public Group update(String updatedName, String updatedIntro, Integer updatedCapacity) {

        if (updatedName == null && updatedIntro == null && updatedCapacity == null) {
            throw new GroupException(ErrorCode.GROUP_NO_INPUT);
        }

        if (updatedCapacity != null && updatedCapacity <= 0) {
            throw new GroupException(ErrorCode.WRONG_GROUP_CAPACITY);
        }

        if (updatedName != null) {
            this.name = updatedName;
        }
        if (updatedIntro != null) {
            this.intro = updatedIntro;
        }
        if (updatedCapacity != null) {
            this.capacity = updatedCapacity;
        }
        return this;
    }

    public void changeCoverImage(String newImageUrl) {
        if (newImageUrl == null || newImageUrl.isEmpty()) {
            this.coverImageUrl = DEFAULT_COVER_IMAGE_URL;
        } else {
            this.coverImageUrl = newImageUrl;
        }
    }

    public String returnDefaultImgUrl() {
        return DEFAULT_COVER_IMAGE_URL;
    }

}
