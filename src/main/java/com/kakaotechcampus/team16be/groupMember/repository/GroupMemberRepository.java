package com.kakaotechcampus.team16be.groupMember.repository;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMemberStatus;
import com.kakaotechcampus.team16be.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByGroupAndUser(Group group, User user);

    List<GroupMember> findAllByGroup(Group group);

    List<GroupMember> findAllByGroupAndStatus(Group group, GroupMemberStatus status);
}
