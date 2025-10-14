package com.kakaotechcampus.team16be.chatroom.repository;

import com.kakaotechcampus.team16be.chatroom.domain.ChatRoom;
import com.kakaotechcampus.team16be.group.domain.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {
    List<ChatRoom> findChatRoomsByGroup(Group group);

}
