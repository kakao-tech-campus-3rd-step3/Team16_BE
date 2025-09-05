package com.kakaotechcampus.team16be.comment.domain;

import com.kakaotechcampus.team16be.member.domain.Member;
import com.kakaotechcampus.team16be.post.domain.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    public Comment(String content, Post post, Member member, Comment parent) {
        this.content = content;
        this.post = post;
        this.member = member;
        this.parent = parent;
    }

    public void update(String content) {
        this.content = content;
    }
}
