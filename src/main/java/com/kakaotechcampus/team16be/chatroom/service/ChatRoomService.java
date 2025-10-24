package com.kakaotechcampus.team16be.chatroom.service;

import com.kakaotechcampus.team16be.chatroom.dto.CreateChatRoomDto;
import com.kakaotechcampus.team16be.chatroom.domain.ChatRoom;
import com.kakaotechcampus.team16be.chatroom.dto.UpdateChatRoomDto;
import com.kakaotechcampus.team16be.user.domain.User;

import java.util.List;

public interface ChatRoomService {
    List<ChatRoom> getAllChatRooms(User user, Long groupId);

    ChatRoom createChatRoom(User user, Long groupId, CreateChatRoomDto createChatRoomDto);

    ChatRoom updateChatRoom(User user, Long groupId, UpdateChatRoomDto updateChatRoomDto, Long chatRoomId);

    void deleteChatRoom(User user, Long groupId, Long chatRoomId);

}
