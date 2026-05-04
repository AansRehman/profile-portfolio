package com.aans.portfolio.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "university_education")
@Getter
@Setter
@NoArgsConstructor
public class UniversityEducation {

  @Id
  private Long id = 1L;

  @Column(nullable = false, length = 400)
  private String school;

  @Column(nullable = false, length = 200)
  private String degree;

  @Column(nullable = false, length = 200)
  private String location;

  @Column(nullable = false, length = 120)
  private String period;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "university_courses", joinColumns = @JoinColumn(name = "education_id"))
  @OrderColumn(name = "course_idx")
  @Column(name = "course_name", length = 200)
  private List<String> courses = new ArrayList<>();
}
