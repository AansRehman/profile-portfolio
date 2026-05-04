package com.aans.portfolio.controllers;

import com.aans.portfolio.models.ProfileResponse;
import com.aans.portfolio.service.PortfolioReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PublicProfileController {

  private final PortfolioReadService portfolioReadService;

  @GetMapping("/api/profile")
  public ProfileResponse profile() {
    return portfolioReadService.getPublicProfile();
  }
}
