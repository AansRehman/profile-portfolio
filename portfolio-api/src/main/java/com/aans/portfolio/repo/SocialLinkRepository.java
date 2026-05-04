package com.aans.portfolio.repo;

import com.aans.portfolio.entities.SocialLinkEntry;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialLinkRepository extends JpaRepository<SocialLinkEntry, Long> {

  List<SocialLinkEntry> findAllByOrderByIdAsc();
}
