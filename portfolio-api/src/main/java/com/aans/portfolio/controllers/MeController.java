package com.aans.portfolio.controllers;

import com.aans.portfolio.models.MeResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {

  @GetMapping("/api/me")
  public MeResponse me(Authentication authentication) {
    String role =
        authentication.getAuthorities().stream()
            .findFirst()
            .map(GrantedAuthority::getAuthority)
            .map(a -> a.replace("ROLE_", ""))
            .orElse("UNKNOWN");
    return new MeResponse(authentication.getName(), role);
  }
}
