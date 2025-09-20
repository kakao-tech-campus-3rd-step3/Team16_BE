package com.kakaotechcampus.team16be.attend.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.plan.domain.Plan;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
/***
 * plan_id, group_member_id에 unique 제약 조건을 추가하여
 * 동일한 그룹 멤버가 동일한 계획에 대해 여러 번 출석할 수 없도록 설정
 */
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"plan_id", "group_member_id"})
        }
)
public class Attend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_member_id", nullable = false)
    private GroupMember groupMember;

    @Enumerated(EnumType.STRING)
    @Column(name = "attend_status", nullable = false)
    private AttendStatus attendStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    protected Attend() {
    }

    @Builder
    public Attend(GroupMember groupMember, Plan plan) {
        this.groupMember = groupMember;
        this.plan = plan;
        this.attendStatus = checkAttendStatus(plan);
    }

    private AttendStatus checkAttendStatus(Plan plan) {
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(plan.getStartTime())) {
            return AttendStatus.PRESENT;
        } else if (!now.isAfter(plan.getEndTime())) {
            return AttendStatus.LATE;
        } else {
            return AttendStatus.ABSENT;
        }
    }


    public static Attend attendPlan(GroupMember groupMember,Plan plan) {
        return Attend.builder()
                .groupMember(groupMember)
                .plan(plan)
                .build();
    }
}
