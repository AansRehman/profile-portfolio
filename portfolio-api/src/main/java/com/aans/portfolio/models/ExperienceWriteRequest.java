package com.aans.portfolio.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record ExperienceWriteRequest(
    @NotBlank String company,
    @JsonProperty("clientOrNote") String clientOrNote,
    @NotBlank @JsonProperty("role") String role,
    @NotBlank String period,
    @NotEmpty List<String> bullets,
    @JsonProperty("includeInResume") Boolean includeInResume) {}
