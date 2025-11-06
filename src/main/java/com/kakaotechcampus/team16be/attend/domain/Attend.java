package com.kakaotechcampus.team16be.attend.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.plan.domain.Plan;
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

    @Column(name = "attend_time")
    private LocalDateTime attendTime;

    protected Attend() {
    }

    @Builder(builderMethodName = "absentBuilder")
    public Attend(GroupMember groupMember, Plan plan, AttendStatus attendStatus) {
        this.groupMember = groupMember;
        this.plan = plan;
        this.attendStatus = attendStatus;
        this.attendTime = LocalDateTime.now();
    }


    @Builder
    public Attend(GroupMember groupMember, Plan plan) {
        this.groupMember = groupMember;
        this.plan = plan;
        this.attendStatus = checkAttendStatus(plan);
        this.attendTime = LocalDateTime.now();
    }

    public static Attend attendPlan(GroupMember groupMember,Plan plan) {
        AttendStatus status = checkAttendStatus(plan);

        return Attend.absentBuilder()
                .groupMember(groupMember)
                .plan(plan)
                .attendStatus(status)
                .build();
    }

    private static AttendStatus checkAttendStatus(Plan plan) {
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(plan.getStartTime())) {
            return AttendStatus.PRESENT;
        } else if (now.isBefore(plan.getEndTime())) {
            return AttendStatus.LATE;
        } else {
            return AttendStatus.ABSENT;
        }
    }


    public static Attend absentPlan(GroupMember groupMember, Plan plan) {
        return Attend.absentBuilder()
                .groupMember(groupMember)
                .plan(plan)
                .attendStatus(AttendStatus.ABSENT)
                .build();
    }

    public static Attend pendingAttendPlan(GroupMember targetGroupMember, Plan plan) {
        return Attend.absentBuilder()
                .groupMember(targetGroupMember)
                .plan(plan)
                .attendStatus(AttendStatus.PENDING)
                .build();
    }

    public void updateStatus(AttendStatus attendStatus) {
        this.attendStatus = attendStatus;
    }
}
