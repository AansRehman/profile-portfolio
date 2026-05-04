package com.aans.portfolio.models;

import java.util.List;

public record EducationDto(String school, String degree, String location, String period, List<String> courses) {}
