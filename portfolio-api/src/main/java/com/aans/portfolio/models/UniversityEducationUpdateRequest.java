package com.aans.portfolio.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record UniversityEducationUpdateRequest(
    @NotBlank String school,
    @NotBlank String degree,
    @NotBlank String location,
    @NotBlank String period,
    @NotEmpty List<String> courses) {}
