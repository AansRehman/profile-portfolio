package com.aans.portfolio.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record ProjectWriteRequest(
    @NotBlank String name,
    @NotBlank String stack,
    @NotBlank String period,
    String demoUrl,
    @NotEmpty List<String> bullets,
    Boolean includeInResume) {}
