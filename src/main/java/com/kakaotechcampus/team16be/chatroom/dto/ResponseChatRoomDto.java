package com.kakaotechcampus.team16be.chatroom.dto;

import com.kakaotechcampus.team16be.chatroom.domain.ChatRoom;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseChatRoomDto(
        String id,
        String roomName,
        LocalDateTime createdAt
) {
    public static List<ResponseChatRoomDto> from(List<ChatRoom> chatRooms) {
        return chatRooms.stream()
                .map(chatRoom -> new ResponseChatRoomDto(
                        chatRoom.getId(),
                        chatRoom.getRoomName(),
                        chatRoom.getCreatedAt()))
                .toList();
    }

    public static ResponseChatRoomDto from(ChatRoom chatRoom) {
        return new ResponseChatRoomDto(
                chatRoom.getId(),
                chatRoom.getRoomName(),
                chatRoom.getCreatedAt()
        );
    }
}

