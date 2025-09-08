package com.kakaotechcampus.team16be.groupMember.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Getter
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

    private LocalDateTime joinAt;

    public GroupMember(Group group, User user, GroupRole role, GroupMemberStatus status) {
        this.group = group;
        this.user = user;
        this.role = role;
        this.status = status;
    }

    protected GroupMember() {

    }
}