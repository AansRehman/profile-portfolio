package com.aans.portfolio.repo;

import com.aans.portfolio.entities.AboutLine;
import com.aans.portfolio.entities.AboutSection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AboutLineRepository extends JpaRepository<AboutLine, Long> {

  List<AboutLine> findBySectionOrderBySortOrderAsc(AboutSection section);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("delete from AboutLine a where a.section = :section")
  void deleteBySection(@Param("section") AboutSection section);
}
