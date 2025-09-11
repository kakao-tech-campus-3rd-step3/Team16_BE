package com.kakaotechcampus.team16be.groupMember.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "group_members")
public class GroupMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @NotNull
    private GroupRole role;


    @Enumerated(EnumType.STRING)
    @NotNull
    private GroupMemberStatus status;

    private LocalDateTime leftAt;

    @CreatedDate
    private LocalDateTime joinAt;

    @Builder
    public GroupMember(Group group, User user, GroupRole role, GroupMemberStatus status) {
        this.group = group;
        this.user = user;
        this.role = role;
        this.status = status;
    }

    protected GroupMember() {

    }
}