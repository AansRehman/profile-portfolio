package com.aans.portfolio.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ProfileResponse(
    @JsonProperty("name") String name,
    String headline,
    String summary,
    List<String> aboutMe,
    List<String> whatIBring,
    List<String> journeyNotes,
    String connectBlurb,
    ContactDto contact,
    EducationDto education,
    List<PriorEducationDto> priorEducation,
    List<ExperienceDto> experience,
    List<SkillCategoryDto> skillCategories,
    List<ProjectDto> projects,
    List<CertificationDto> certifications) {}
