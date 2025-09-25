package com.kakaotechcampus.team16be.planParticipant.domain;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanParticipant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "plan_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Plan plan;

  @JoinColumn(name = "group_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Group group;

  @JoinColumn(name = "user_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @Column(name = "participant_status", nullable = false)
  @Enumerated(EnumType.STRING)
  private ParticipantStatus participantStatus;

  @Builder
  public PlanParticipant(Plan plan, Group group, User user, ParticipantStatus participantStatus) {
    this.plan = plan;
    this.group = group;
    this.user = user;
    this.participantStatus = ParticipantStatus.ATTEND;
  }
}
