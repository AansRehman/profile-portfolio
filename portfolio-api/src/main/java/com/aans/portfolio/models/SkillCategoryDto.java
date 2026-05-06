package com.aans.portfolio.models;

import java.util.List;

public record SkillCategoryDto(String title, List<String> items, boolean includeInResume) {}
