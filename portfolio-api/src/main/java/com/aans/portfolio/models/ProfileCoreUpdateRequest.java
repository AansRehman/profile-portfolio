package com.aans.portfolio.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProfileCoreUpdateRequest(
    @NotBlank String name,
    @NotBlank String headline,
    @NotBlank String summary,
    @NotBlank String connectBlurb,
    @NotNull @Valid ContactDto contact) {}
