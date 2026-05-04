package com.aans.portfolio.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ExperienceAdminDto(
    Long id,
    String company,
    @JsonProperty("clientOrNote") String clientOrNote,
    @JsonProperty("role") String role,
    String period,
    List<String> bullets) {}
