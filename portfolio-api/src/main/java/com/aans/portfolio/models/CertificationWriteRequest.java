package com.aans.portfolio.models;

import jakarta.validation.constraints.NotBlank;

public record CertificationWriteRequest(
    @NotBlank String title, @NotBlank String issuer, String date, String url) {}
