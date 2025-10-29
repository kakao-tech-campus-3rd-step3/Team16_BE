package com.kakaotechcampus.team16be.report.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reports") //report 예약어로 인한 이름 변경.
public class Report extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reporter_id", nullable = false)
  private User reporter;

  @Enumerated(EnumType.STRING)
  @Column(name = "target_type", nullable = false)
  private TargetType targetType;

  @Column(name = "target_id", nullable = false)
  private Long targetId;

  @Enumerated(EnumType.STRING)
  @Column(name = "reason_code", nullable = false)
  private ReasonCode reasonCode;

  @Column(name = "reason", nullable = false)
  private String reason;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ReportStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "resolved_by")
  private User resolvedBy;

  @Column(name = "resolved_at")
  private LocalDateTime resolvedAt;

    @Column(name = "penalty_applied", nullable = false)
    private boolean penaltyApplied = false;

  @Builder
  public Report(User reporter, TargetType targetType, Long targetId, ReasonCode reasonCode, String reason){
    if(reporter == null || targetType == null || targetId == null)
      throw new IllegalArgumentException("필수 값이 누락되었습니다.");
    if(reason == null || reason.isBlank())
      throw new IllegalArgumentException("신고 사유가 비어 있습니다.");

    this.reporter = reporter;
    this.targetType = targetType;
    this.targetId = targetId;
    this.reasonCode = reasonCode;
    this.reason = reason;
    this.status = ReportStatus.PENDING;
    this.penaltyApplied = false;
  }

  public void resolve(User resolvedUser, ReportStatus reportStatus){

      if (reportStatus == null) {
          throw new IllegalArgumentException("신고 처리 상태는 null일 수 없습니다.");
      }

    if(reportStatus == ReportStatus.PENDING){
      throw new IllegalArgumentException("신고 상태는 PENDING으로 변경할 수 없습니다.");
    }
    this.status = reportStatus;
    this.resolvedBy = resolvedUser;
    this.resolvedAt = LocalDateTime.now();
  }

  public enum ReasonCode{
    RELIGION_SUSPECT,
    NOT_HEALTHY_PURPOSE,
    INAPPROPRIATE,
    FRAUD_OR_PRIVACY,
    OTHER
  }
    public void markPenaltyApplied() {
        if (this.status == null) {
            this.status = ReportStatus.PENDING;
        }

        this.penaltyApplied = true;
    }
}
