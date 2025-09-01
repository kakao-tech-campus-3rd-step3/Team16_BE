package com.kakaotechcampus.team16be.group.repository;

import com.kakaotechcampus.team16be.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group,Long> {
}
