package com.aans.portfolio.models;

public record PriorEducationAdminDto(
    Long id, String school, String credential, String period, boolean includeInResume) {}
