package com.kakaotechcampus.team16be.groupMember.domain;

public enum GroupRole {
    LEADER,
    MEMBER;

    public boolean isLeader() {
        return this == LEADER;
    }

    public boolean isMember() {
        return this == MEMBER;
    }
}
