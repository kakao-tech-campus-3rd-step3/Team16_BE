package com.kakaotechcampus.team16be.groupMember.domain;

public enum GroupMemberStatus {
    ACTIVE,
    LEFT,
    BANNED,
    PENDING,
    CANCELED;

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isLeft() {
        return this == LEFT;
    }

    public boolean isBanned() {
        return this == BANNED;
    }

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isCanceled() {
        return this == CANCELED;
    }
}
