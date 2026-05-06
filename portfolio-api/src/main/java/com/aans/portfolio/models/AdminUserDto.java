package com.aans.portfolio.models;

import com.aans.portfolio.entities.Role;

public record AdminUserDto(Long id, String username, Role role) {}
