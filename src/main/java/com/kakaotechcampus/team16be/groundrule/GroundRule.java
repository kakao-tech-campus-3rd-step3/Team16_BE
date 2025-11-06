package com.kakaotechcampus.team16be.groundrule;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.group.domain.Group;
import com.kakaotechcampus.team16be.group.exception.GroupErrorCode;
import com.kakaotechcampus.team16be.group.exception.GroupException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.Getter;

@Entity
@Getter
public class GroundRule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column(columnDefinition = "TEXT")
    private String content;

    protected GroundRule() {
    }

    public static GroundRule create(Group group, String content) {
        GroundRule groundRule = new GroundRule();
        groundRule.group = group;
        groundRule.changeContent(content);
        return groundRule;
    }

    public void changeContent(String newContent) {
        this.content = newContent;
    }

    public boolean belongsTo(Long groupId) {
        return Objects.equals(group.getId(), groupId);
    }

    public void validateAccess(Long groupId) {
        if (!belongsTo(groupId)) {
            throw new GroupException(GroupErrorCode.WRONG_GROUP_ACCESS);
        }
    }
}
