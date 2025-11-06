package com.kakaotechcampus.team16be.post.repository;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.post.domain.Post;
import com.kakaotechcampus.team16be.user.domain.User;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    Optional<Post> findByIdAndGroup(Long id, Group group);

    List<Post> findByGroup(Group group);

    Optional<Post> findByAuthorAndId(User author, Long id);

    boolean existsByAuthorAndCreatedAtAfter(User author, LocalDateTime startOfDay);

    @EntityGraph(attributePaths = {"author"})
    List<Post> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"author"})
    List<Post> findByGroupOrderByCreatedAtDesc(Group group);
}
