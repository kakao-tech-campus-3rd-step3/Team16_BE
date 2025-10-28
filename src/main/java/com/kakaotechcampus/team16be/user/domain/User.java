package com.kakaotechcampus.team16be.user.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "users")
public class User extends BaseEntity {

    private final static Integer ATTENDANCE = 10;
    private final static Integer ABSENT = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_id", unique = true, nullable = false, length = 64)
    private String kakaoId;

    @Column(name = "nickname", unique = true, length = 40)
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

    protected User() {}

    public User(String kakaoId) {
        this.kakaoId = kakaoId;
        this.nickname = "익명";
        this.role = Role.USER;
        this.verificationStatus = VerificationStatus.UNVERIFIED;
    }

    @Builder
    public User(Long id, String kakaoId, String nickname, String profileImageUrl, Role role, VerificationStatus verificationStatus, String studentIdImageUrl, Long score) {
        this.id = id;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.verificationStatus = verificationStatus;
        this.studentIdImageUrl = studentIdImageUrl;
        this.score = score;
    }

    public void updateStudentIdImageUrl(String fileName) {
        this.studentIdImageUrl = fileName;
    }

    public void updateVerificationStatusPending() {
        this.verificationStatus = VerificationStatus.PENDING;
    }

    public void updateProfileImageUrl(String fileName) {
        this.profileImageUrl = fileName;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isStudentVerified() {
        return this.verificationStatus == VerificationStatus.VERIFIED;
    }

    public void increaseScoreByAttendance() {
        this.score += ATTENDANCE;
    }

    public void decreaseScoreByAbsent() {
        this.score -= 5;

    }
}
