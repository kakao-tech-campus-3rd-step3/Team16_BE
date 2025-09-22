package com.kakaotechcampus.team16be.comment.domain;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.user.domain.User;
import com.kakaotechcampus.team16be.post.domain.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> childComments = new ArrayList<>();

    @Builder
    public Comment(String content, Post post, User user, Comment parentComment) {
        this.content = content;
        this.post = post;
        this.user = user;
        this.parentComment = parentComment;
    }

    public static Comment createComment(String content, Post post, User user, Comment parentComment) {
        return Comment.builder()
                .content(content)
                .post(post)
                .user(user)
                .parentComment(parentComment)
                .build();
    }


    public void updateContent(String content) {
        this.content = content;
    }


    public void updateParentCommentNull() {
        this.parentComment = null;
    }
}
