package com.aans.portfolio.models;

import java.util.List;

public record ProjectDto(
    String name,
    String stack,
    String period,
    List<String> bullets,
    String demoUrl,
    boolean includeInResume) {}
