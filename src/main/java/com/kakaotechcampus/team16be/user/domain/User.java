package com.kakaotechcampus.team16be.user.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "users")
public class User extends BaseEntity {

    private final static Double ATTENDANCE = 0.30;
    private final static Double POSTING = 0.15;
    private final static Double ABSENT = 1.00;
    private final static Double COMMENT = 0.05;
    private final static Double REPORT = 3.00;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_id", unique = true, nullable = false, length = 64)
    private String kakaoId;

    @Column(name = "nickname", length = 40)
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

    @Column(name = "score", nullable = false)
    private Double score = 40.0;

    protected User() {
    }

    public User(String kakaoId) {
        this.kakaoId = kakaoId;
        this.nickname = "익명";
        this.role = Role.USER;
        this.verificationStatus = VerificationStatus.UNVERIFIED;
        this.score = 40.0;
    }

    @Builder
    public User(Long id, String kakaoId, String nickname, String profileImageUrl, Role role,
                VerificationStatus verificationStatus, String studentIdImageUrl) {
        this.id = id;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.verificationStatus = verificationStatus;
        this.studentIdImageUrl = studentIdImageUrl;
        this.score = 40.0;
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

    public void updateVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public void updateScore(Double score) {
        if (score >= 0) {
            this.score = score;
        }
    }

    public boolean isStudentVerified() {
        return this.verificationStatus == VerificationStatus.VERIFIED;
    }

    public void increaseScoreByAttendance() {
        this.score += ATTENDANCE;
    }

    public void decreaseScoreByAbsent() {
        this.score -= ABSENT;
    }

    public void increaseScoreByPosting() {
        this.score += POSTING;
    }

    public void increaseScoreByComment() {
        this.score += COMMENT;
    }

    public void decreaseScoreByReport() {
        this.score -= REPORT;
    }
}
