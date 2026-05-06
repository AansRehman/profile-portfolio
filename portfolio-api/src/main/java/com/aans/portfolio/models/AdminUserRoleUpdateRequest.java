package com.aans.portfolio.models;

import com.aans.portfolio.entities.Role;
import jakarta.validation.constraints.NotNull;

public record AdminUserRoleUpdateRequest(@NotNull Role role) {}
