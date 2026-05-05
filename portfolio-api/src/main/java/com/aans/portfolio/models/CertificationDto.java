package com.aans.portfolio.models;

public record CertificationDto(
    String title, String issuer, String date, String url, boolean includeInResume) {}
