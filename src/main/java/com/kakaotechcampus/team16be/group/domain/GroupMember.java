package com.kakaotechcampus.team16be.group.domain;

import com.kakaotechcampus.team16be.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public GroupMember(Group group, Member member) {
        this.group = group;
        this.member = member;
    }
}
