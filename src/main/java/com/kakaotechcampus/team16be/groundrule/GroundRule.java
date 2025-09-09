package com.kakaotechcampus.team16be.groundrule;

import com.kakaotechcampus.team16be.common.BaseEntity;
import com.kakaotechcampus.team16be.group.domain.Group;
import jakarta.persistence.*;
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

  protected GroundRule() {}

  public static GroundRule create(Group group, String content) {
    GroundRule groundRule = new GroundRule();
    groundRule.group = group;
    groundRule.changeContent(content);
    return groundRule;
  }

  public void changeContent(String newContent) {
    this.content = newContent;
  }
}
