package com.kakaotechcampus.team16be.group.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.group.exception.ErrorCode;
import com.kakaotechcampus.team16be.group.exception.GroupException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "groups")
public class Group extends BaseEntity {

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
    @Builder.Default
    private SafetyTag safetyTag = SafetyTag.SAFE;

    public Group(String name, String intro, Integer capacity) {
        this.name = name;
        this.intro = intro;
        this.capacity = capacity;
    }

    protected Group() {

    }

    @Builder
    public Group(Long id, String name, String intro, String coverImageUrl, String category, Integer capacity, SafetyTag safetyTag) {
        this.id = id;
        this.name = name;
        this.intro = intro;
        this.coverImageUrl = coverImageUrl;
        this.category = category;
        this.capacity = capacity;
        this.safetyTag = safetyTag;
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
        this.capacity = updatedCapacity;
        return this;
    }
}