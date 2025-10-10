package com.kakaotechcampus.team16be.chatroom.service;

import com.kakaotechcampus.team16be.chatroom.dto.CreateChatRoomDto;
import com.kakaotechcampus.team16be.chatroom.domain.ChatRoom;
import com.kakaotechcampus.team16be.chatroom.dto.UpdateChatRoomDto;
import com.kakaotechcampus.team16be.chatroom.exception.ChatRoomErrorCode;
import com.kakaotechcampus.team16be.chatroom.exception.ChatRoomException;
import com.kakaotechcampus.team16be.chatroom.repository.ChatRoomRepository;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.service.GroupService;
import com.kakaotechcampus.team16be.groupMember.service.GroupMemberService;
import com.kakaotechcampus.team16be.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final GroupMemberService groupMemberService;
    private final GroupService groupService;

    @Transactional(readOnly = true)
    @Override
    public List<ChatRoom> getAllChatRooms(User user, Long groupId) {
        Group targetGroup  = groupService.findGroupById(groupId);
        groupMemberService.validateGroupMember(user,groupId);

        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByGroup(targetGroup);

        return chatRoomRepository.findChatRoomsByGroup(targetGroup);
    }

    @Transactional
    @Override
    public ChatRoom createChatRoom(User user, Long groupId, CreateChatRoomDto createChatRoomDto) {
        Group targetGroup  = groupService.findGroupById(groupId);
        targetGroup.checkLeader(user);

        ChatRoom createdChatRoom = ChatRoom.create(createChatRoomDto.name());

        return chatRoomRepository.save(createdChatRoom);
    }

    @Transactional
    @Override
    public ChatRoom updateChatRoom(User user, Long groupId, UpdateChatRoomDto updateChatRoomDto, Long chatRoomId) {
        Group targetGroup  = groupService.findGroupById(groupId);
        targetGroup.checkLeader(user);

        ChatRoom targetRoom = findChatRoomById(chatRoomId);
        targetRoom.checkGroup(targetGroup);

        ChatRoom updatedRoom = targetRoom.updateName(updateChatRoomDto.name());

        return chatRoomRepository.save(updatedRoom);
    }

    public ChatRoom findChatRoomById(long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomException(ChatRoomErrorCode.CHATROOM_NOT_FOUND));
    }

    public void deleteChatRoom(User user, Long groupId, Long chatRoomId) {
        Group targetGroup  = groupService.findGroupById(groupId);
        targetGroup.checkLeader(user);

        ChatRoom targetRoom = findChatRoomById(chatRoomId);
        targetRoom.checkGroup(targetGroup);

        chatRoomRepository.delete(targetRoom);
    }


}
