package com.kakaotechcampus.team16be.plan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "latitude", nullable = false)
  private Double latitude;

  @Column(name = "longtitude", nullable = false)
  private Double longitude;

  @Builder
  public Location(String name, Double latitude, Double longitude) {
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
