package com.aans.portfolio.models;

import jakarta.validation.constraints.NotBlank;

public record PriorEducationWriteRequest(
    @NotBlank String school,
    @NotBlank String credential,
    @NotBlank String period,
    Boolean includeInResume) {}
