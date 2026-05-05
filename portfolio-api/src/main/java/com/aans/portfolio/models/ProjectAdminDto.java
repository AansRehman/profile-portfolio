package com.aans.portfolio.models;

import java.util.List;

public record ProjectAdminDto(
    Long id,
    String name,
    String stack,
    String period,
    String demoUrl,
    List<String> bullets,
    boolean includeInResume) {}
