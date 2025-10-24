package com.kakaotechcampus.team16be.chatroom.domain;

import com.kakaotechcampus.team16be.chatroom.exception.ChatRoomErrorCode;
import com.kakaotechcampus.team16be.chatroom.exception.ChatRoomException;
import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.group.domain.Group;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @Column(name = "room_id")
    private String id;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoomUser> participants = new HashSet<>();

    public static ChatRoom create(String roomName) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.id = UUID.randomUUID().toString();
        chatRoom.roomName = roomName;
        return chatRoom;
    }

    public long getUserCount() {
        return this.participants.size();
    }

    public void checkGroup(Group targetGroup) {
        if(!this.group.equals(targetGroup)) {
            throw new ChatRoomException(ChatRoomErrorCode.CHATROOM_GROUP_MISMATCH);
        }
    }

    public ChatRoom updateName(String roomName) {
        this.roomName = roomName;
        return this;

    }
}
