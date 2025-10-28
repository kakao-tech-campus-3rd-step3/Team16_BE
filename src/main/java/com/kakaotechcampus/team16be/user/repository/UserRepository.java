package com.kakaotechcampus.team16be.user.repository;

import com.kakaotechcampus.team16be.user.domain.User;
import java.lang.ScopedValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKakaoId(String kakaoId);

    User findByNickname(String nickname);
}
