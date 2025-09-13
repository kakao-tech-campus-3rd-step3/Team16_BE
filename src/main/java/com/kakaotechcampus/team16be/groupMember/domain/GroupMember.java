package com.kakaotechcampus.team16be.groupMember.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.groupMember.exception.GroupMemberException;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;

import static com.kakaotechcampus.team16be.groupMember.domain.GroupMemberStatus.*;
import static com.kakaotechcampus.team16be.groupMember.domain.GroupRole.*;
import static com.kakaotechcampus.team16be.groupMember.exception.ErrorCode.*;

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

    public static GroupMember join(Group group, User user) {

        return GroupMember.builder().
                group(group).
                user(user).
                role(MEMBER).
                status(ACTIVE).
                build();
    }

    public static void checkLeftGroup(GroupMember member) {
        if (member.getRole() == GroupRole.LEADER) {
            throw new GroupMemberException(LEADER_CANNOT_LEAVE);
        }

        if (member.getStatus() == LEFT) {
            throw new GroupMemberException(MEMBER_ALREADY_LEFT);
        }

    }

    public static GroupMember create(Group createdGroup, User user) {
        return GroupMember.builder().
                group(createdGroup).
                user(user).role(LEADER).
                status(ACTIVE).
                build();
    }

    public void join() throws GroupMemberException {
        if (this.status == LEFT) {
            this.status = ACTIVE;
        }
        if (this.status == ACTIVE) {
            throw new GroupMemberException(GROUP_MEMBER_ALREADY_EXIST);
        }
        if (this.status == GroupMemberStatus.BANNED) {
            throw new GroupMemberException(MEMBER_HAS_BANNED);
        }else
            this.status = ACTIVE;
    }

    public void leaveGroup() {
        this.status = LEFT;
        this.leftAt = LocalDateTime.now();
    }

    public void bannedGroup() {
        if (this.status == BANNED) {
            throw new GroupMemberException(MEMBER_ALREADY_BANNED);
        }
        this.status = BANNED;
        this.leftAt = LocalDateTime.now();
    }

    public void checkUserInGroup(User user) {
        if (!this.user.getId().equals(user.getId())) {
            throw new GroupMemberException(GROUP_MEMBER_ALREADY_EXIST);
        }

    }

}