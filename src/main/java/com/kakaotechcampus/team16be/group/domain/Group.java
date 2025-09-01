package com.kakaotechcampus.team16be.group.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "groups") // Group 예약어로 인한 변경
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min=4)
    private String name;

    @NotNull
    private String intro;

    private String coverImageUrl;

    private String category;

    @Min(0)
    private Integer capacity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SafetyTag safetyTag;

    /***
     * UserId만 받아오기 VS Group에 아예 User 객체를 받기
     *
     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "leaderUserId", nullable = false)
//    private User leader;

}
