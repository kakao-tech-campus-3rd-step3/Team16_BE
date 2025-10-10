package com.kakaotechcampus.team16be.group.repository;

import com.kakaotechcampus.team16be.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsGroupByName(String name);

    @Query("SELECT gm.group.id FROM GroupMember gm WHERE gm.user.id = :userId AND gm.status = 'ACTIVE'")
    List<String> findMemberGroupIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT g.id FROM Group g WHERE g.leader.id = :userId")
    List<String> findLeaderGroupIdsByUserId(@Param("userId") Long userId);
}
