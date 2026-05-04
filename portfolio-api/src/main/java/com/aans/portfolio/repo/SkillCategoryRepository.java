package com.aans.portfolio.repo;

import com.aans.portfolio.entities.SkillCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillCategoryRepository extends JpaRepository<SkillCategory, Long> {

  List<SkillCategory> findAllByOrderBySortOrderAsc();
}
