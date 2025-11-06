package com.kakaotechcampus.team16be.review.memberReview.domain;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class MemberReview {
    @Enumerated(EnumType.STRING)
    Evaluation evaluation;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id", nullable = false)
    private User reviewee;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
    @NotBlank
    private String content;

    @Builder
    public MemberReview(User reviewer, User reviewee, Group group, String content, Evaluation evaluation) {
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.group = group;
        this.content = content;
        this.evaluation = evaluation;
    }

    protected MemberReview() {
    }

    public static MemberReview create(User reviewer, User reviewee, Group targetGroup, String content,
                                      Evaluation evaluation) {
        return MemberReview.builder().
                reviewee(reviewee).
                reviewer(reviewer).
                group(targetGroup).
                content(content).
                evaluation(evaluation).
                build();
    }
}
