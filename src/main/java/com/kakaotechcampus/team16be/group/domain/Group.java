package com.kakaotechcampus.team16be.group.domain;

import ch.qos.logback.core.util.StringUtil;
import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.group.exception.GroupErrorCode;
import com.kakaotechcampus.team16be.group.exception.GroupException;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.Builder;
import lombok.Getter;


@Entity
@Getter
@Table(name = "groups")
public class Group extends BaseEntity {

    private final static Double REPORT = 3.00;
    private final static Double PLANNING = 0.1;
    private final static Double POSTING = 0.05;
    private final static Double CAUTION_GROUP = 74.0;
    private final static Double DANGER_GROUP = 62.0;


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

    @Min(1)
    private Integer currentCapacity = 1;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SafetyTag safetyTag = SafetyTag.SAFE;

    private final SafetyTag safetyTagFinal = SafetyTag.SAFE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaderUserId", nullable = false)
    private User leader;

    @Column(name = "score", nullable = false)
    private Double score = 80.0;


    protected Group() {
    }

    @Builder
    public Group(User user, String name, String intro, Integer capacity,String fileName) {
        this.leader = user;
        this.name = name;
        this.intro = intro;
        this.capacity = capacity;
        if (StringUtil.isNullOrEmpty(fileName)) {
            this.coverImageUrl = "";
        }else
            this.coverImageUrl = fileName;
        this.score = 80.0;
    }

    public static Group createGroup(User user, String name, String intro, Integer capacity, String fileName) {
        return Group.builder().
                user(user).
                name(name).
                intro(intro).
                capacity(capacity).
                fileName(fileName).
                build();
    }

    public Group update(String updatedName, String updatedIntro, Integer updatedCapacity) {
        if (updatedCapacity != null && updatedCapacity <= 0) {
            throw new GroupException(GroupErrorCode.WRONG_GROUP_CAPACITY);
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
        this.coverImageUrl = newImageUrl;
    }


    public void checkLeader(User user) {
        if (!(this.leader == user)) {
            throw new GroupException(GroupErrorCode.WRONG_GROUP_LEADER);
        }
    }

    public void changeLeader(User user) {
        this.leader = user;
    }

    public void decreaseScoreByReport() {
        this.score -= REPORT;
    }

    public void increaseScoreByPosting() {
        this.score += POSTING;
    }

    public void increaseScoreByPlanning() {
        this.score += PLANNING;
    }

    public void groupScoreUpdate(Double avg) {
        BigDecimal rounded = BigDecimal.valueOf(avg)
                .setScale(2, RoundingMode.HALF_UP);
        this.score = rounded.doubleValue();
    }

    public void updateSafetyTagByScore() {
        if (this.score > CAUTION_GROUP) {
            this.safetyTag = SafetyTag.SAFE;
        } else if (this.score > DANGER_GROUP) {
            this.safetyTag = SafetyTag.CAUTION;
        } else {
            this.safetyTag = SafetyTag.DANGER;
        }
    }

    public void updateSafetyTag(SafetyTag safetyTag) {
        this.safetyTag = safetyTag;
    }
}
