package com.aans.portfolio.controllers;

import com.aans.portfolio.models.ProfileResponse;
import com.aans.portfolio.service.PortfolioReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ResumeSourceController {

  private final PortfolioReadService portfolioReadService;

  /** Data source for LaTeX resume generation (pre-filtered by includeInResume flags). */
  @GetMapping("/api/resume/source")
  public ProfileResponse resumeSource() {
    return portfolioReadService.getResumeProfile();
  }
}
