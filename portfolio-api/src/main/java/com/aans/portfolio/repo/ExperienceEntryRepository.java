package com.aans.portfolio.repo;

import com.aans.portfolio.entities.ExperienceEntry;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceEntryRepository extends JpaRepository<ExperienceEntry, Long> {

  List<ExperienceEntry> findAllByOrderByIdAsc();
}
