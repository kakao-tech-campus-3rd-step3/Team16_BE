package com.kakaotechcampus.team16be.group.repository;

import com.kakaotechcampus.team16be.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsGroupByName(String name);
}
