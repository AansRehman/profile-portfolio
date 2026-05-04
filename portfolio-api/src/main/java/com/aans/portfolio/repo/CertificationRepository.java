package com.aans.portfolio.repo;

import com.aans.portfolio.entities.CertificationEntry;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationRepository extends JpaRepository<CertificationEntry, Long> {

  List<CertificationEntry> findAllByOrderByIdAsc();
}
