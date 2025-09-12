package com.kakaotechcampus.team16be.post.repository;

import com.kakaotechcampus.team16be.post.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.group.id = :groupId AND (:cursor IS NULL OR p.id < :cursor) AND (:search IS NULL OR p.title LIKE CONCAT('%', :search, '%') OR p.content LIKE CONCAT('%', :search, '%')) ORDER BY p.id DESC")
    Slice<Post> findByGroupIdWithCursor(@Param("groupId") Long groupId, @Param("cursor") Long cursor, @Param("search") String search, Pageable pageable);
}
