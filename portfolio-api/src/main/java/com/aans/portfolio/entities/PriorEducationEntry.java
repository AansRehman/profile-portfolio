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
@Table(name = "prior_education")
@Getter
@Setter
@NoArgsConstructor
public class PriorEducationEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 300)
  private String school;

  @Column(nullable = false, length = 300)
  private String credential;

  @Column(nullable = false, length = 120)
  private String period;
}
