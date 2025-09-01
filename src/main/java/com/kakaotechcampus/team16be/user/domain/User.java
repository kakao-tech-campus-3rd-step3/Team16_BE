package com.kakaotechcampus.team16be.user.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakaoId", length = 64)
    private String kakaoId;

    @Column(name = "nickname", length = 40)
    private String nickname;

    @Column(name = "profileImageUrl", length = 512)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "verificationStatus")
    private VerificationStatus verificationStatus;

    @Column(name = "studentIdImageUrl", length = 512)
    private String studentIdImageUrl;

    @Column(name = "groupId")
    private Long groupId;

    @Column(name = "score")
    private Long score;
}
