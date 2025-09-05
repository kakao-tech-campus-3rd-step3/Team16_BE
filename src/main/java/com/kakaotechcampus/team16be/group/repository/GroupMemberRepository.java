package com.kakaotechcampus.team16be.group.repository;

import com.kakaotechcampus.team16be.group.domain.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
}
