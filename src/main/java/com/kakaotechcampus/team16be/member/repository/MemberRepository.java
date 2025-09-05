package com.kakaotechcampus.team16be.member.repository;

import com.kakaotechcampus.team16be.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
