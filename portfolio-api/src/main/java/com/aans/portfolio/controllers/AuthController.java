package com.aans.portfolio.controllers;

import com.aans.portfolio.entities.AppUser;
import com.aans.portfolio.models.LoginRequest;
import com.aans.portfolio.models.LoginResponse;
import com.aans.portfolio.repo.AppUserRepository;
import com.aans.portfolio.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final AppUserRepository appUserRepository;
  private final JwtService jwtService;

  @PostMapping("/api/auth/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(req.username(), req.password()));
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    AppUser user = appUserRepository.findByUsername(req.username()).orElseThrow();
    return ResponseEntity.ok(
        new LoginResponse(jwtService.generateToken(user), user.getUsername(), user.getRole().name()));
  }
}
