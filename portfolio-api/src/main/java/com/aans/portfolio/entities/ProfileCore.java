package com.aans.portfolio.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "profile_core")
@Getter
@Setter
@NoArgsConstructor
public class ProfileCore {

  @Id
  private Long id = 1L;

  @Column(nullable = false, length = 200)
  private String personName;

  @Column(nullable = false, length = 300)
  private String headline;

  @Column(nullable = false, length = 4000)
  private String summary;

  @Column(nullable = false, length = 4000)
  private String connectBlurb;

  @Column(nullable = false, length = 40)
  private String phone;

  @Column(nullable = false, length = 200)
  private String email;

  @Column(nullable = false, length = 500)
  private String github;

  @Column(nullable = false, length = 500)
  private String linkedin;
}
