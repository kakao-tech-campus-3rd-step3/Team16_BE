package com.kakaotechcampus.team16be.comment.repository;

import com.kakaotechcampus.team16be.comment.domain.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND ((:parentId IS NULL AND c.parent IS NULL) OR (c.parent.id = :parentId)) AND (:cursor IS NULL OR c.id > :cursor) ORDER BY c.id ASC")
    Slice<Comment> findByPostIdAndParentIdWithCursor(@Param("postId") Long postId, @Param("parentId") Long parentId, @Param("cursor") Long cursor, Pageable pageable);
}
