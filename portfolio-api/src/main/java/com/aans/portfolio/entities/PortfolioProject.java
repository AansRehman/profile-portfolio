package com.aans.portfolio.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "portfolio_projects")
@Getter
@Setter
@NoArgsConstructor
public class PortfolioProject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 300)
  private String name;

  @Column(nullable = false, length = 400)
  private String stack;

  @Column(nullable = false, length = 120)
  private String period;

  @Column(length = 800)
  private String demoUrl;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "project_bullets", joinColumns = @JoinColumn(name = "project_id"))
  @OrderColumn(name = "line_idx")
  @Column(name = "bullet_text", length = 4000)
  private List<String> bullets = new ArrayList<>();
}
