package com.kakaotechcampus.team16be.groupMember.repository;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMember;
import com.kakaotechcampus.team16be.groupMember.domain.GroupMemberStatus;
import com.kakaotechcampus.team16be.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByGroupAndUser(Group group, User user);

    List<GroupMember> findAllByGroup(Group group);

    List<GroupMember> findAllByGroupAndStatus(Group group, GroupMemberStatus status);

    @Query("SELECT gm FROM GroupMember gm " +
            "WHERE gm.user.id = :userId AND gm.status IN :statuses " +
            "ORDER BY gm.joinAt DESC")
    List<GroupMember> findAllByUserIdAndStatusIn(
            @Param("userId") Long userId,
            @Param("statuses") List<GroupMemberStatus> statuses
    );

    Optional<GroupMember> findByUserAndGroup(User signedUser, Group targetGroup);

    void deleteAllByUserId(Long userId);

    List<GroupMember> findAllByUser(User user);
}
