package com.aans.portfolio.service;

import com.aans.portfolio.entities.AppUser;
import com.aans.portfolio.entities.Role;
import com.aans.portfolio.models.AdminUserCreateRequest;
import com.aans.portfolio.models.AdminUserDto;
import com.aans.portfolio.repo.AppUserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class AdminUserService {

  private final AppUserRepository appUserRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public List<AdminUserDto> listUsers() {
    return appUserRepository.findAll().stream()
        .map(u -> new AdminUserDto(u.getId(), u.getUsername(), u.getRole()))
        .toList();
  }

  @Transactional
  public AdminUserDto createUser(AdminUserCreateRequest req) {
    String username = req.username().trim();
    if (appUserRepository.findByUsername(username).isPresent()) {
      throw new ResponseStatusException(CONFLICT, "Username already exists");
    }
    AppUser user = new AppUser();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(req.password()));
    user.setRole(req.role());
    AppUser saved = appUserRepository.save(user);
    return new AdminUserDto(saved.getId(), saved.getUsername(), saved.getRole());
  }

  @Transactional
  public AdminUserDto updateRole(long id, Role role) {
    AppUser user =
        appUserRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
    user.setRole(role);
    AppUser saved = appUserRepository.save(user);
    return new AdminUserDto(saved.getId(), saved.getUsername(), saved.getRole());
  }
}
