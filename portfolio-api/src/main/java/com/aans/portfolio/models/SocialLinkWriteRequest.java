package com.aans.portfolio.models;

import jakarta.validation.constraints.NotBlank;

public record SocialLinkWriteRequest(
    @NotBlank String label, @NotBlank String url, Boolean includeInResume) {}
