package com.aans.portfolio.repo;

import com.aans.portfolio.entities.PortfolioProject;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioProjectRepository extends JpaRepository<PortfolioProject, Long> {

  List<PortfolioProject> findAllByOrderByIdAsc();
}
