package com.aans.portfolio.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record SkillCategoryWriteRequest(@NotBlank String title, @NotNull List<String> items) {}
