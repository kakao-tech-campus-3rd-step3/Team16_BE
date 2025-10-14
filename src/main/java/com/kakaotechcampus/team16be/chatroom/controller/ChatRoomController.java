package com.kakaotechcampus.team16be.chatroom.controller;


import com.kakaotechcampus.team16be.chatroom.domain.ChatRoom;
import com.kakaotechcampus.team16be.chatroom.dto.CreateChatRoomDto;
import com.kakaotechcampus.team16be.chatroom.dto.ResponseChatRoomDto;
import com.kakaotechcampus.team16be.chatroom.dto.UpdateChatRoomDto;
import com.kakaotechcampus.team16be.chatroom.service.ChatRoomService;
import com.kakaotechcampus.team16be.common.annotation.LoginUser;
import com.kakaotechcampus.team16be.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/{groupId}/chat")
    public ResponseEntity<List<ResponseChatRoomDto>> getAllChatRooms(@LoginUser User user, @PathVariable Long groupId) {
        List<ChatRoom> chatRooms = chatRoomService.getAllChatRooms(user, groupId);
        return ResponseEntity.ok(ResponseChatRoomDto.from(chatRooms));
    }

    @PostMapping("/{groupId}/chat")
    public ResponseEntity<ResponseChatRoomDto> createChatRoom(@LoginUser User user, @PathVariable Long groupId, @RequestBody CreateChatRoomDto createChatRoomDto) {
        ChatRoom chatRoom = chatRoomService.createChatRoom(user, groupId, createChatRoomDto);

        return ResponseEntity.ok(ResponseChatRoomDto.from(chatRoom));
    }

    @PutMapping("/{groupId}/chat/{chatRoomId}")
    public ResponseEntity<ResponseChatRoomDto> updateChatRoom(@LoginUser User user, @PathVariable Long groupId, @RequestBody UpdateChatRoomDto updateChatRoomDto, @PathVariable Long chatRoomId) {
        ChatRoom chatRoom = chatRoomService.updateChatRoom(user, groupId, updateChatRoomDto, chatRoomId);

        return ResponseEntity.ok(ResponseChatRoomDto.from(chatRoom));
    }

    @DeleteMapping("/{groupId}/chat/{chatRoomId}")
    public ResponseEntity<Void> deleteChatRoom(@LoginUser User user, @PathVariable Long groupId, @PathVariable Long chatRoomId) {
        chatRoomService.deleteChatRoom(user, groupId, chatRoomId);

        return ResponseEntity.ok().build();
    }
}
