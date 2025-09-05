package com.kakaotechcampus.team16be.post.domain;

import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private int viewCount;

    @ElementCollection
    private List<String> imageUrls;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Post(String title, String content, List<String> imageUrls, Member member, Group group) {
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.member = member;
        this.group = group;
        this.viewCount = 0;
    }

    public void update(String title, String content, List<String> imageUrls) {
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
    }
}
