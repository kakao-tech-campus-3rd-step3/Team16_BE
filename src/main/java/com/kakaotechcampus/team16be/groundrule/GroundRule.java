package com.kakaotechcampus.team16be.groundrule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
public class GroundRule extends BaseEntity{

  @Id
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


