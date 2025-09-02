package com.kakaotechcampus.team16be.group.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "groups") // Group 예약어로 인한 변경
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String intro;

    private String coverImageUrl;

    private String category;

    @Min(0)
    private Integer capacity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SafetyTag safetyTag = SafetyTag.안전;

    //    @ManyToOne(fetch = FetchType.LAZY)
    //    @JoinColumn(name = "leaderUserId", nullable = false)
    //    private User leader;

    public Group(String name, String intro, Integer capacity) {
        this.name = name;
        this.intro = intro;
        this.capacity = capacity;
    }

    protected Group() {

    }

    public Group update(String updatedName, String updatedIntro, Integer updatedCapacity) {

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



}
