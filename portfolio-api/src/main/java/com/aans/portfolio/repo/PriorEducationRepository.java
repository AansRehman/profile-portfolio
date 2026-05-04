package com.aans.portfolio.repo;

import com.aans.portfolio.entities.PriorEducationEntry;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriorEducationRepository extends JpaRepository<PriorEducationEntry, Long> {

  List<PriorEducationEntry> findAllByOrderByIdAsc();
}
