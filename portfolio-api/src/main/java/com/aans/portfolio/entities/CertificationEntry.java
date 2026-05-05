package com.aans.portfolio.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "certifications")
@Getter
@Setter
@NoArgsConstructor
public class CertificationEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 500)
  private String title;

  @Column(nullable = false, length = 200)
  private String issuer;

  @Column(length = 64)
  private String dateAwarded;

  /** Optional public link (credential / badge / issuer page). */
  @Column(length = 2000)
  private String certificateUrl;

  @Column
  private Boolean includeInResume = true;
}
