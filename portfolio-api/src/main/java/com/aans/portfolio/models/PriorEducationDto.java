package com.aans.portfolio.models;

public record PriorEducationDto(
    String school, String credential, String period, boolean includeInResume) {}
