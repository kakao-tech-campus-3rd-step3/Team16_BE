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
  private Long groupId;

  @OneToOne(fetch = FetchType.LAZY) //그룹 당 그라운드 룰 1개.
  @MapsId
  @JoinColumn(name = "group_id")
  private Group group; //후에 코드 통합 시 모임 도메인 받아오기.

  @Column(columnDefinition = "TEXT")
  private String content;

  protected GroundRule() {}

  public static GroundRule create(Group group, String content){
    GroundRule groundRule = new GroundRule();
    groundRule.group = group;
    groundRule.groupId = group.getId();
    groundRule.changeContent(content);
    return groundRule;
  }

  public void changeContent(String newContent) {
    this.content = newContent;
  }
}


