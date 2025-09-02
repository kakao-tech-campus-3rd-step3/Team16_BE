package com.kakaotechcampus.team16be.user.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_id", unique = true, nullable = false, length = 64)
    private String kakaoId;

    @Column(name = "nickname", unique = true, nullable = false, length = 40)
    private String nickname;

    @Column(name = "profile_image_url", length = 512)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    private VerificationStatus verificationStatus;

    @Column(name = "student_id_image_url", length = 512)
    private String studentIdImageUrl;

    @Column(name = "score")
    private Long score;
}
