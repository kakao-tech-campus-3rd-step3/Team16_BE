package com.kakaotechcampus.team16be.groupMember.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.exception.GroupException;
import com.kakaotechcampus.team16be.groupMember.exception.GroupMemberException;
import com.kakaotechcampus.team16be.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;

import static com.kakaotechcampus.team16be.group.exception.GroupErrorCode.WRONG_GROUP_ACCESS;
import static com.kakaotechcampus.team16be.groupMember.domain.GroupMemberStatus.*;
import static com.kakaotechcampus.team16be.groupMember.domain.GroupRole.*;
import static com.kakaotechcampus.team16be.groupMember.exception.GroupMemberErrorCode.*;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "group_members",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"group_id", "user_id"})
        }
)
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

    private String intro;

    @CreatedDate
    private LocalDateTime joinAt;

    @Builder
    public GroupMember(Group group, User user, GroupRole role, GroupMemberStatus status,String intro) {
        this.group = group;
        this.user = user;
        this.role = role;
        this.status = status;
        this.intro = intro;
    }

    protected GroupMember() {

    }

    public static GroupMember acceptJoin(Group group, User user) {

        return GroupMember.builder().
                group(group).
                user(user).
                role(MEMBER).
                status(ACTIVE).
                build();
    }

    public static void checkLeftGroup(GroupMember member) {
        if (member.getRole().equals(LEADER)) {
            throw new GroupMemberException(LEADER_CANNOT_LEAVE);
        }

        if (member.getStatus().equals(LEFT)) {
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

    public static GroupMember sign(User signedUser, Group targetGroup, String intro) {
        return GroupMember.builder().
                group(targetGroup).
                user(signedUser).role(MEMBER).
                status(PENDING).
                intro(intro).
                build();
    }

    public void acceptJoin() throws GroupMemberException {
        if (this.status.equals(LEFT)) {
            this.status = ACTIVE;
        }
        if (this.status.equals(ACTIVE)) {
            throw new GroupMemberException(GROUP_MEMBER_ALREADY_EXIST);
        }
        if (this.status.equals(BANNED)) {
            throw new GroupMemberException(MEMBER_HAS_BANNED);
        } else
            this.status = ACTIVE;
    }

    public void leaveGroup() {
        this.status = LEFT;
        this.leftAt = LocalDateTime.now();
    }

    public void bannedGroup() {
        if (this.status.equals(BANNED)) {
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


    public void changeToLeader() {
        if (this.role.equals(LEADER)) {
            throw new GroupException(WRONG_GROUP_ACCESS);
        }
        this.role = LEADER;
        this.group.changeLeader(this.user);
    }


    public void changeToMember() {
        if (!(this.role.equals(MEMBER))) {
            throw new GroupException(WRONG_GROUP_ACCESS);
        }
        this.role = MEMBER;
    }

    public void cancelSignGroup() {
        if (this.status.equals(PENDING)) {
            this.status = CANCELED;
        } else
            throw new GroupMemberException(MEMBER_CANNOT_CANCEL);

    }

    public void checkUserIsActive() {
        if (!this.status.equals(ACTIVE)) {
            throw new GroupMemberException(GROUP_MEMBER_NOT_FOUND);
        }
    }

}