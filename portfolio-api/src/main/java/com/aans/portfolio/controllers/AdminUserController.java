package com.aans.portfolio.controllers;

import com.aans.portfolio.models.AdminUserCreateRequest;
import com.aans.portfolio.models.AdminUserDto;
import com.aans.portfolio.models.AdminUserRoleUpdateRequest;
import com.aans.portfolio.service.AdminUserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

  private final AdminUserService adminUserService;

  @GetMapping
  public List<AdminUserDto> listUsers() {
    return adminUserService.listUsers();
  }

  @PostMapping
  public AdminUserDto createUser(@Valid @RequestBody AdminUserCreateRequest body) {
    return adminUserService.createUser(body);
  }

  @PutMapping("/{id}/role")
  public AdminUserDto updateRole(@PathVariable long id, @Valid @RequestBody AdminUserRoleUpdateRequest body) {
    return adminUserService.updateRole(id, body.role());
  }
}
